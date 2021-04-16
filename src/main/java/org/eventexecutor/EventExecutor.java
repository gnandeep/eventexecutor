package org.eventexecutor;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An wrapper around {@link ScheduledExecutorService } that can trigger a one
 * time or periodic tasks/actions.
 * 
 * @author gnani
 *
 */
public class EventExecutor {

	// made it constant and fixed for simplicity
	private int threadPoolSize = 5;

	// configurable thread name - should be defined while initializing the
	// constructor
	private String processName;

	// configurable delay
	private int delay;

	// configurable periodicRun
	private int periodicRun;

	// configurable isDaeomon
	private boolean isDaeomon;

	EventExecutor(String processName) {
		this.processName = processName;
	}

	EventExecutor() {
	}

	void setPeriodicRun(int periodicRun) {
		this.periodicRun = periodicRun;
	}

	void setDelay(int delay) {
		this.delay = delay;
	}

	void setDaeomon(boolean isDaeomon) {
		this.isDaeomon = isDaeomon;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	// logger
	static final Logger logger = LogManager.getLogger(EventExecutor.class);

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
	public <T> void runAsync(T request, Consumer<T> task) throws Exception {
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
			throw ex;
		}

	}

	/**
	 * 
	 * Execute a {@link Consumer} lambda using {@link T} request object.
	 * 
	 * This is a one time action. This is a blocking call.
	 * 
	 * @param <T>
	 * @param request
	 * @param task
	 * @return true , if execution is successful, else false.
	 * @throws Exception, if failed to schedule a task.
	 */
	public <T> boolean runSync(T request, Consumer<T> task) throws Exception {
		try {
			logger.debug("Launching {} event in {} seconds.", processName, delay);

			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadPoolSize,
					new SimpleThreadFactory(processName, false));

			// wrapper for runnable.
			AsyncTask<T> asyncTask = new AsyncTask<>(request, task);

			ScheduledFuture<?> result = null;

			result = scheduledExecutorService.schedule(asyncTask, delay, TimeUnit.SECONDS);

			// block thread
			if (Objects.nonNull(result) && Objects.isNull(result.get()))
				return true;

			logger.debug("{} event triggered.", processName);

		} catch (Exception ex) {
			logger.error("{} exception triggered. error: {}", ex.getMessage());
			throw ex;
		}
		return false;

	}

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

	class AsyncTask<T> implements Runnable {
		private T request;
		private Consumer<T> task;

		AsyncTask(T request, Consumer<T> task) {
			this.request = request;
			this.task = task;
		}

		@Override
		public void run() {
			task.accept(request);

		}
	}

}
