package storm.demo;

import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCountBolt implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167430982072644778L;
	String redisURL;
	public WordCountBolt redis(String url) { redisURL = url;return this; }
	public String redis() { return redisURL; }
	
	
	transient OutputCollector collector;
	transient HashSet<String> update;
	transient RedisConnection<String,String> redis;
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		update = new HashSet<String>();
		RedisClient client = new RedisClient(redisURL);
		redis = client.connect();
		(new Timer("updater")).schedule(new TimerTask() {

			@Override
			public void run() {
				HashSet<String> todo;
				synchronized(update) {
					todo = update;
					update = new HashSet<String>();
				}
				for(String w : todo) 
					redis.zadd("WordCount", redis.scard("Word:"+w), w);
				if(todo.size() > 0)
					redis.publish("WordCount", ""+todo.size());
			}
			
		}, 1000, 1000);
	}

	public void execute(Tuple input) {
		Integer line    = input.getIntegerByField("line");
		String  speaker = input.getStringByField("speaker");
		String  text    = input.getStringByField("text");
		
		for(String word : text.toLowerCase().split("\\s+")) {
			redis.sadd("Word:"+word, ""+line);
			redis.sadd("Words", word);
			synchronized(update) { update.add(word); }
		}
		redis.incr("Lines");
		redis.sadd("Speakers", speaker);
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
