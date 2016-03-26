package org.akhil.zeromq.pubsub_envelope;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Pub {
	
	public static void main(String[] args) throws InterruptedException {
		Context context = ZMQ.context(1);
		Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5566");
		
		while(true) {
			double rand = Math.random();
			int n = (int) (rand*100);
			int mod = n%3;
			publisher.sendMore(mod+"");
			publisher.send(n+"");
			System.out.println("publishing : " + n);
			Thread.sleep(1000);
			
		}
		
		
	}

}
