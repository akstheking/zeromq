package org.akhil.zmq.poll;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Poll {
	
	public static void main(String[] args) {
		Context context = ZMQ.context(1);
		Socket socket = context.socket(ZMQ.PULL);
//		Socket socket2 = 
		socket.connect("tcp://127.0.0.1:1234");
		
		while (!Thread.currentThread().isInterrupted()) {
			String msg = socket.recvStr();
			System.out.println("REcevied : " + msg);
		}
		/*ZMQ.Poller items = new ZMQ.Poller(1);
		items.register(socket, ZMQ.Poller.POLLIN);
//		poll.register()
		while(!Thread.currentThread().isInterrupted()) {
			long i = items.poll();
			if(items.pollin(0)) {
				System.out.println("pollin on 0");
				String msg = socket.recvStr();
				System.out.println("REceived : " + msg);
			}
		}*/
		
		socket.close();
		context.close();
	}

}
