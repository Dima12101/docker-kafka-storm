package org.apache.storm.starter.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SplitterBolt
 */
public class SplitterBolt extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(SplitterBolt.class);
    private OutputCollector collector;

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {

        // get tuples from KafkaSpout
        String [] words = tuple.getString(0).split("\\s+");

        for (int i = 0; i < words.length; i++) {
            // check fo non-word character and replace
            words[i] = words[i].replaceAll("[^\\w]", "");

            // emit words and acknowledge processed tuple
            collector.emit(tuple, new Values(words[i]));
            collector.ack(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}
