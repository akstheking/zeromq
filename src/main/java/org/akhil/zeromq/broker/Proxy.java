package org.akhil.zeromq.broker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Proxy {
	
	public static void main(String[] args) {
		Context context = ZMQ.context(2);
		Socket frontend = context.socket(ZMQ.ROUTER);
		frontend.bind("tcp://*:5554");
		Socket backend = context.socket(ZMQ.DEALER);
		backend.bind("tcp://*:5555");
		ZMQ.proxy(frontend, backend, null);
	}

}
