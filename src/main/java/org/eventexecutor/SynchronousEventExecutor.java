package org.eventexecutor;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.eventexecutor.builder.threadfactory.SimpleThreadFactory;
import org.eventexecutor.dataobjects.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronousEventExecutor extends EventExecutor {

	SynchronousEventExecutor(String processName) {
		setProcessName(processName);
	}

	SynchronousEventExecutor() {

	}

	// logger
	static final Logger logger = LoggerFactory.getLogger(SynchronousEventExecutor.class);

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
	public <T> boolean run(T request, Consumer<T> task) throws Exception {
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

}
