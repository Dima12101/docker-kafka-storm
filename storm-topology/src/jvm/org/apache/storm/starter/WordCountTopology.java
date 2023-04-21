package org.apache.storm.starter;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.Config;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.starter.bolt.CounterBolt;
import org.apache.storm.starter.bolt.RankerBolt;
import org.apache.storm.starter.bolt.SplitterBolt;
import org.apache.storm.starter.spout.DataSourceSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Arrays;


/**
 * WordCountTopology
 *
 * The Storm topology "wires" the various computation steps
 * done in the Spout and Bolts together to a DAG via defined
 * stream groupings (shuffle grouping, field grouping, global
 * grouping).
 */
public class WordCountTopology extends ConfigurableTopology {

    private static final Logger LOG = LoggerFactory.getLogger(WordCountTopology.class);

    private static final String DATASOURCE_SPOUT_ID = "datasource-spout";
    private static final String KAFKA_SPOUT_ID = "kafka-spout";
    private static final String SPLITTER_BOLT_ID = "splitter-bolt";
    private static final String COUNTER_BOLT_ID = "counter-bolt";
    private static final String RANKER_BOLT_ID = "ranker-bolt";

    public static void main(String[] args) throws Exception {
        ConfigurableTopology.start(new WordCountTopology(), args);
    }

    /**
     * WordCountTopology with Kafka
     *
     * @return      TopologyBuilder Object
     */
    public TopologyBuilder builderTopology(String ZK_HOST, String ZK_PORT, String TOPIC) {
        
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(KAFKA_SPOUT_ID, new KafkaSpout<>(KafkaSpoutConfig.builder(ZK_HOST + ":" + ZK_PORT, TOPIC).build()));
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4).shuffleGrouping(KAFKA_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4).fieldsGrouping(SPLITTER_BOLT_ID, new Fields("word"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt()).globalGrouping(COUNTER_BOLT_ID);

        return builder;
    }

    /**
     * WordCountTopology without Kafka
     *
     * @return      TopologyBuilder Object
     */
    public TopologyBuilder builderTopology() {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(DATASOURCE_SPOUT_ID, new DataSourceSpout());
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4).shuffleGrouping(DATASOURCE_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4).fieldsGrouping(SPLITTER_BOLT_ID, new Fields("word"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt()).globalGrouping(COUNTER_BOLT_ID);

        return builder;
    }

    protected int run(String[] args) {
        Config conf = new Config();
        String TOPOLOGY_NAME;

        if (args != null && args.length > 0) {
            TOPOLOGY_NAME = args[0];
            /**
             * Remote deployment as part of Docker Compose multi-application setup
             *
             * @TOPOLOGY_NAME:       Name of Storm topology
             * @ZK_HOST:             Host IP address of ZooKeeper
             * @ZK_PORT:             Port of ZooKeeper
             * @TOPIC:               Kafka Topic which this Storm topology is consuming from
             */
            LOG.info("Submitting topology " + TOPOLOGY_NAME + " to remote cluster.");
            String ZK_HOST = args[1];
            int ZK_PORT = Integer.parseInt(args[2]);
            String TOPIC = args[3];

            conf.setDebug(false);
            conf.setNumWorkers(2);
            conf.setMaxTaskParallelism(5);
            conf.put(Config.STORM_ZOOKEEPER_PORT, ZK_PORT);
            conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(ZK_HOST));

            return submit(TOPOLOGY_NAME, conf, builderTopology(String.valueOf(ZK_PORT), ZK_HOST, TOPIC));
        }
        else {
            TOPOLOGY_NAME = "wordcount-topology";
            /**
             * Local mode (only for testing purposes)
             */
            LOG.info("Starting topology " + TOPOLOGY_NAME + " in LocalMode.");

            conf.setDebug(false);
            conf.setNumWorkers(2);
            conf.setMaxTaskParallelism(2);

            return submit(TOPOLOGY_NAME, conf, builderTopology());
        }
    }
}