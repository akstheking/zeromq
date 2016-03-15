package org.akhil.zmq.clientserver;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Client {

	public static void main(String[] args) {
		
		Context context = ZMQ.context(1);
		Socket socket = context.socket(ZMQ.REQ);
		socket.connect("tcp://localhost:5555");
		socket.send("Ho");
		String recv = socket.recvStr();
		System.out.println("Receveid from server : " + recv);
	}

}
