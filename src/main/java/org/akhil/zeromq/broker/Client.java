package org.akhil.zeromq.broker;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Client {

	public static void main(String[] args) {
		
		Context context = ZMQ.context(1);
		Socket socket = context.socket(ZMQ.REQ);
		socket.connect("tcp://localhost:5554");
		String item = (args.length <= 0)?"DEFAULT":args[0];
		socket.send(item);
		String recv = socket.recvStr();
		System.out.println("Receveid from server : " + recv);
		socket.close();
		context.close();
	}

}
