package org.akhil.zeromq.broker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Broker {

	public static void main(String[] args) {
		Context context = ZMQ.context(2);
		Socket frontend = context.socket(ZMQ.ROUTER);
		frontend.bind("tcp://*:5554");
		Socket backend = context.socket(ZMQ.DEALER);
		backend.bind("tcp://*:5555");

		ZMQ.Poller items = new ZMQ.Poller(2);
		items.register(frontend);
		items.register(backend);
		boolean more = false;
		byte[] msg;
		while (!Thread.currentThread().isInterrupted()) {
			items.poll();
			if (items.pollin(0)) {
				while (true) {
					msg = frontend.recv();
					System.out.println("Broker client : " + msg);
					more = frontend.hasReceiveMore();
					backend.send(msg, more ? ZMQ.SNDMORE : 0);
					if(!more) {
						break;
					}
				}
			}
			if (items.pollin(1)) {
				while (true) {
					msg = backend.recv();
					System.out.println("Broker server : " + msg);
					more = backend.hasReceiveMore();
					frontend.send(msg, more ? ZMQ.SNDMORE : 0);
					if(!more) {
						break;
					}
				}
			}
		}
		frontend.close();
		backend.close();
		context.close();
	}

}
