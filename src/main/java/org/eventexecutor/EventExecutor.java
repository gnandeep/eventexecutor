package org.eventexecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * An wrapper around {@link ScheduledExecutorService } that can trigger a one
 * time or periodic tasks/actions.
 * 
 * @author gnani
 *
 */

public abstract class EventExecutor {

	// made it constant and fixed for simplicity
	protected int threadPoolSize = 5;

	// configurable thread name - should be defined while initializing the
	// constructor
	protected String processName;

	// configurable delay
	protected int delay;

	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public abstract <T> boolean run(T request, Consumer<T> task) throws Exception;



}
