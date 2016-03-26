package org.akhil.zeromq.pair;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Pair {

	public static void main(String[] args) throws InterruptedException {
		Context context = ZMQ.context(1);
		Thread step1 = new Thread(new Step1(context));
		Thread step2 = new Thread(new Step2(context));
		Thread step3 = new Thread(new Step3(context));
		System.out.println("Step3 started");
		step3.start();
		Thread.sleep(3000);
		System.out.println("Step2 started");
		step2.start();
		Thread.sleep(4000);
		System.out.println("STep3 started");
		step1.start();
		Thread.sleep(5000);
		context.close();
	}

	private static class Step1 implements Runnable {

		Context context;

		public Step1(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			Socket step2 = context.socket(ZMQ.PAIR);
			step2.connect("inproc://step2");
			System.out.println("STep1 signalling step2");
			// Just to show you cannot connect more then 
			// two socket to a pair inproc
			/*
			 * Socket dummy = context.socket(ZMQ.PAIR);
			 * dummy.connect("inproc://step2"); dummy.send("dummy");
			 */
			step2.send("READY");
			step2.close();
		}

	}

	private static class Step2 implements Runnable {

		Context context;

		public Step2(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			Socket step2 = context.socket(ZMQ.PAIR);
			step2.bind("inproc://step2");
			String recv = step2.recvStr();
			System.out.println("STep2 receives : " + recv);
			step2.close();
			Socket step3 = context.socket(ZMQ.PAIR);
			step3.connect("inproc://step3");
			System.out.println("STep2 signalling step3");
			step3.send("READY");
			step3.close();
		}

	}

	private static class Step3 implements Runnable {

		Context context;

		public Step3(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			Socket step3 = context.socket(ZMQ.PAIR);
			step3.bind("inproc://step3");
			String recv = step3.recvStr();
			System.out.println("STep3 received : " + recv);
			step3.close();
		}

	}

}
