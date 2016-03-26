package org.akhil.zeromq.broker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class MTServer {
	
	private final static int NUMBER_WORKERS = 4;

	public static void main(String[] args) {
		Context context = ZMQ.context(2);
		Socket frontend = context.socket(ZMQ.ROUTER);
		frontend.bind("tcp://*:5554");
		Socket backend = context.socket(ZMQ.DEALER);
		backend.bind("inproc://workers");
		for(int i=0; i< NUMBER_WORKERS; i++) {
			new Thread(new Worker(context)).start();
		}
		ZMQ.proxy(frontend, backend, null);

	}

	private static class Worker implements Runnable{

		Context context;

		public Worker(Context context) {
			this.context = context;
		}

		public void run() {
			Socket rep = context.socket(ZMQ.REP);
			rep.connect("inproc://workers");
			while (true) {
				String recv = rep.recvStr();
				System.out.println("Thread : " + Thread.currentThread() + " received : " + recv);
				int length = recv.length();
				rep.send(length + "");
			}
		}
	}

}
