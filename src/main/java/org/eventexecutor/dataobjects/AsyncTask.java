package org.eventexecutor.dataobjects;

import java.util.function.Consumer;

public class AsyncTask<T> implements Runnable {
	private T request;
	private Consumer<T> task;

	public AsyncTask(T request, Consumer<T> task) {
		this.request = request;
		this.task = task;
	}

	@Override
	public void run() {
		task.accept(request);

	}
}

