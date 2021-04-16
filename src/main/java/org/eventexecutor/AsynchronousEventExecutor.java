package org.eventexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.eventexecutor.builder.threadfactory.SimpleThreadFactory;
import org.eventexecutor.dataobjects.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchronousEventExecutor extends EventExecutor {

	// configurable periodicRun
	private int periodicRun;

	// configurable isDaeomon
	private boolean isDaeomon;

	AsynchronousEventExecutor(String processName) {
		this.processName = processName;
	}

	AsynchronousEventExecutor() {
	}

	public void setPeriodicRun(int periodicRun) {
		this.periodicRun = periodicRun;
	}

	public void setDaeomon(boolean isDaeomon) {
		this.isDaeomon = isDaeomon;
	}

	// logger
	static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

	/**
	 * 
	 * Execute a {@link Consumer} lambda using {@link T} request object.
	 * 
	 * If daemon setting is configured, this would run periodically for the define
	 * periodicRun run, if not a one time action is invoked.
	 * 
	 * @param <T>
	 * @param request
	 * @param task
	 * @throws Exception , if failed to schedule a task.
	 */
	public <T> boolean run(T request, Consumer<T> task) throws Exception {
		try {
			logger.debug("Launching {} event in {} seconds.", processName, delay);

			// custom thread factory to configure daemon thread and thread name.
			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadPoolSize,
					new SimpleThreadFactory(processName, this.isDaeomon));

			// wrapper for runnable.
			AsyncTask<T> asyncTask = new AsyncTask<>(request, task);

			if (isDaeomon) {
				logger.debug("Setting up {} as periodic event. periodicRun: {}", processName, periodicRun);
				scheduledExecutorService.scheduleAtFixedRate(asyncTask, delay, periodicRun, TimeUnit.SECONDS);
			} else {
				logger.debug("Launching {} event in {} seconds.", processName, delay);
				scheduledExecutorService.schedule(asyncTask, delay, TimeUnit.SECONDS);
			}

			logger.debug("{} event triggered.", processName);

		} catch (Exception ex) {
			logger.error("{} exception triggered. error: {}", ex.getMessage());
			return false;
		}
		return true;
	}

}
