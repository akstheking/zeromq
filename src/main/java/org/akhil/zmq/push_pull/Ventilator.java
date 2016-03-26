package org.akhil.zmq.push_pull;

import java.io.File;
import java.io.IOException;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Ventilator {

	public static void main(String[] args) throws InterruptedException, IOException {
		Context context = ZMQ.context(2);
		Socket ventialtor = context.socket(ZMQ.PUSH);
		ventialtor.bind("tcp://*:5567");
		Socket sink = context.socket(ZMQ.PUSH);
		sink.connect("tcp://127.0.0.1:5568");
		System.out.println("Press Enter when the workers are ready: ");
        System.in.read();
        System.out.println("Sending tasks to workers\n");
        File dir = new File("/home/akhil/tmp/tasks");
        File[] files = dir.listFiles();
        System.out.println("Number of files : " + files.length);
        sink.send(files.length + "", ZMQ.DONTWAIT);
		for (File file : files) {
			Thread.sleep(30);
			ventialtor.send(file.getAbsolutePath());
		}
		sink.close();
		ventialtor.close();
		context.term();
	}

}
