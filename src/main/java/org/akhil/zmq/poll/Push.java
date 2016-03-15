package org.akhil.zmq.poll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Push {
	
	public static void main(String[] args) throws IOException {
		Context context = ZMQ.context(1);
		Socket socket = context.socket(ZMQ.PUSH);
		socket.bind("tcp://127.0.0.1:1234");
		//socket.send("fkdfjal");
//		socket.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while((line = br.readLine()) != null) {
			socket.send(line);
		}
		/*Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			String line = in.next();
			socket.send(line);
		}*/
	}

}
