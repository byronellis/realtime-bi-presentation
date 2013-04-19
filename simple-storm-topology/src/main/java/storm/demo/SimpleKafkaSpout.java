package storm.demo;

import java.util.Map;

import scala.collection.Iterator;

import kafka.api.FetchRequest;
import kafka.consumer.SimpleConsumer;
import kafka.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import kafka.utils.Utils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SimpleKafkaSpout implements IRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6600483485543838832L;

	/* Configuration Options */
	String topic   = "topic";
	String host    = "127.0.0.1";
	int    port    = 9092;
	int    timeout = 10000;
	int    buffer  = 102400;
	
	public SimpleKafkaSpout topic(String topic) { this.topic = topic;return this; }
	public String     topic() { return topic; }
	
	public SimpleKafkaSpout host(String host) { this.host = host;return this; }
	public String           host() { return host; }
	
	public SimpleKafkaSpout port(int port) { this.port = port;return this; }
	public int port() { return port; }
	
	public SimpleKafkaSpout timeout(int timeout) { this.timeout = timeout; return this; }
	public int timeout() { return timeout; }
	
	public SimpleKafkaSpout buffer(int buffer) { this.buffer = buffer;return this; }
	public int buffer() { return buffer; }
	
	
	
	transient SpoutOutputCollector collector;
	transient SimpleConsumer       consumer;
	transient long                 offset;
	
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		consumer = new SimpleConsumer(host,port,timeout,buffer);
		offset   = 0;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}

	public void nextTuple() {
		FetchRequest req = new FetchRequest(topic,0,offset,1024*1024);
		ByteBufferMessageSet messages = consumer.fetch(req);
		Iterator<MessageAndOffset> iter = messages.iterator();
		while(iter.hasNext()) {
			MessageAndOffset msg = iter.next();
			collector.emit(new Values(Utils.toString(msg.message().payload(), "UTF-8")));
			offset = msg.offset();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			collector.reportError(e);
		}
	}

	public void ack(Object msgId) {
		// TODO Auto-generated method stub

	}

	public void fail(Object msgId) {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
