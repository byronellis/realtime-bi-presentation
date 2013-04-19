package storm.demo;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class Topology {
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", (new SimpleKafkaSpout()).topic("hamlet"));
		builder.setBolt("event", new EventParserBolt())
			.shuffleGrouping("spout");
		builder.setBolt("words", (new WordCountBolt()).redis("localhost"))
			.shuffleGrouping("event");
		Map conf = new HashMap();
		conf.put(Config.TOPOLOGY_WORKERS, 1);
		conf.put(Config.TOPOLOGY_DEBUG, "true");
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("demo-topology", conf, builder.createTopology());
	}
}
