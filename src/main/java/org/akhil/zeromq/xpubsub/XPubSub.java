package org.akhil.zeromq.xpubsub;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class XPubSub {
	
	public static void main(String[] args) {
		Context context = ZMQ.context(1);
		Socket xsub = context.socket(ZMQ.XSUB);
		xsub.bind("tcp://*:5565");
		xsub.subscribe("".getBytes());
		Socket xpub = context.socket(ZMQ.XPUB);
		xpub.bind("tcp://*:5566");
		while (true) {
			String recv = xsub.recvStr();
			System.out.println("X-received : " + recv);
			xpub.send(recv);
		}

	}

}
