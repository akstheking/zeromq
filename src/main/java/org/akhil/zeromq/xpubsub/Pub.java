package org.akhil.zeromq.xpubsub;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Pub {
	
	public static void main(String[] args) throws InterruptedException {
		Context context = ZMQ.context(1);
		Socket publisher = context.socket(ZMQ.PUB);
		publisher.connect("tcp://127.0.0.1:5565");
		
		while(true) {
			double rand = Math.random();
			int n = (int) (rand*100);
			publisher.send(n+"");
			System.out.println("publishing : " + n);
			Thread.sleep(1000);
			
		}
		
		
	}

}
