package org.eventexecutor.builder.threadfactory;

import java.util.concurrent.ThreadFactory;

 public class SimpleThreadFactory implements ThreadFactory {
	String name;

	boolean isDaemon;

	public SimpleThreadFactory(String name, boolean isDaemon) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, name);
		thread.setDaemon(isDaemon);
		return thread;

	}
}
