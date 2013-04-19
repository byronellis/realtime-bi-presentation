package storm.demo;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class EventParserBolt implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8131515295719242137L;
	transient OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line","speaker","text"));
	}

	public void execute(Tuple input) {
			String[] parts = input.getStringByField("message").split("\t");
			
			Integer line = Integer.parseInt(parts[0]);
			String  speaker = parts[1];
			String  text    = parts[2].replaceAll("\\p{Punct}|\\d"," ");
			collector.emit(new Values(line,speaker,text));
			collector.ack(input);
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}


	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
