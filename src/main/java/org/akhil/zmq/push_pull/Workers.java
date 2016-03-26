package org.akhil.zmq.push_pull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Workers {

	private static List<Integer> wait = new LinkedList<>();

	public static void main(String[] args) throws InterruptedException {
		int noOfWrokers = 4;
		ForkJoinPool pool = new ForkJoinPool(noOfWrokers);
		List<Worker> workers = new ArrayList<>();
		for (int i = 0; i < noOfWrokers; i++) {
			new Thread(new Worker(i)).start();
		}
		System.out.println("all workers here");
		Thread.sleep(3000);
		System.out.println("all workers exit");
	}

	private static class Worker implements Runnable {

		private int id;

		public Worker(int id) {
			this.id = id;
		}

		public void run() {
			Context context = ZMQ.context(1);
			Socket pull = context.socket(ZMQ.PULL);
			pull.connect("tcp://127.0.0.1:5567");
			Socket sink = context.socket(ZMQ.PUSH);
			sink.connect("tcp://127.0.0.1:5568");
			Socket sub = context.socket(ZMQ.SUB);
			sub.connect("tcp://127.0.0.1:5569");
			sub.subscribe("".getBytes());

			ZMQ.Poller items = new ZMQ.Poller(2);
			items.register(pull, ZMQ.Poller.POLLIN);
			items.register(sub, ZMQ.Poller.POLLIN);

			while (true) {
				long ret = items.poll(2000);
				System.out.println("polled : " + ret);
				if (items.pollin(0)) {
					String file = pull.recvStr();
					long l = 0L;
					try {
						BufferedReader br = new BufferedReader(new FileReader(
								file));
						while (br.readLine() != null) {
							l += 1L;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(id * 200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sink.send(l + "");
					System.out.println("Worker : " + id + " ::: " + file
							+ " => " + l);
				}
				if (items.pollin(1)) {
					String recv = sub.recvStr();
					System.out.println("REceived kill cmd : " + recv);
					break;
				}
			}
			System.out.println("Worker : " + id + " exits");
			sub.close();
			sink.close();
			pull.close();
			context.close();
		}

	}
}
