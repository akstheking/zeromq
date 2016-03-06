package org.akhil.zmq.clientserver;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Server {
	 
	public static void main(String[] args) {
		Context context = ZMQ.context(3);
		Socket socket = context.socket(ZMQ.REP);
		socket.bind("tcp://*:5555");
		while(!Thread.currentThread().isInterrupted()) {
			byte[] msg = socket.recv();
			String str = new String(msg);
			System.out.println("Received : " + str);
			socket.send(str + " HIHIHIHI");
		}
	}

}
