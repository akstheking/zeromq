package org.akhil.zmq.push_pull;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Sink {
	
	public static void main(String[] args) throws InterruptedException {
		Context context = ZMQ.context(1);
		Socket pub = context.socket(ZMQ.PUB);
		pub.bind("tcp://*:5569");
		Socket sink = context.socket(ZMQ.PULL);
		sink.bind("tcp://127.0.0.1:5568");
		String noOfFiles = sink.recvStr();
		int num = Integer.parseInt(noOfFiles);
		System.out.println("Number of files" + num);
		Long totalLines = 0L;
		while((num--) > 0) {
			System.out.println(num);
			String length = sink.recvStr();
			long size = Long.parseLong(length);
			System.out.println("Received " + num + " : " + length);
			totalLines += size;
		}
		System.out.println("total files : " + noOfFiles);
		System.out.println("total : " + totalLines);
		Thread.sleep(2000);
		pub.send("END");
		sink.close();
		pub.close();
		context.term();
	}

}
