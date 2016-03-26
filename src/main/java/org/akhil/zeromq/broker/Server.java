package org.akhil.zeromq.broker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Server {
	 
	public static void main(String[] args) {
		Context context = ZMQ.context(3);
		String id = (args.length <= 0)?"UNKNOWN":args[0];
		Socket socket = context.socket(ZMQ.REP);
		socket.connect("tcp://127.0.0.1:5555");
		System.out.println("Started server " + id);
		while(!Thread.currentThread().isInterrupted()) {
			byte[] msg = socket.recv();
			String str = new String(msg);
			System.out.println("Request recevied by server " + id + " : " + str);
			socket.send("Server " + id + " => " + str.length());
		}
	}

}
