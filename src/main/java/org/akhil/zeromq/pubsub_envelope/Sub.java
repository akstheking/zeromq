package org.akhil.zeromq.pubsub_envelope;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Sub {

	public static void main(String[] args) {
		Context context = ZMQ.context(1);
		Socket sub = context.socket(ZMQ.SUB);
		sub.connect("tcp://127.0.0.1:5566");
		String topicString = (args.length > 0)?args[0]:"1";
		String[] topics = topicString.split(",");
		for(String topic : topics) {
			sub.subscribe(topic.getBytes());
		}
		boolean more = false;
		while (true) {
			String topic = sub.recvStr();
			more = sub.hasReceiveMore();
			String recv = sub.recvStr();
			System.out.println("REceived : " + topic + " == " + recv);
		}

	}

}
