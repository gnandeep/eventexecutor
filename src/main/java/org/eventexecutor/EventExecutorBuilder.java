package org.eventexecutor;

/**
 * 
 * A object builder class for EventExecutor. By default delay is 0 and
 * periodicRun is 1.
 * 
 * @author gnani
 *
 */
public class EventExecutorBuilder {

	protected boolean daemonProcess;
	protected String processName;
	protected int delay = 0;
	protected int periodicRun = 1;
	protected int threadPoolSize = 4;

	/**
	 * ProcessName passed will be used as name for the thread pool.
	 * 
	 * @param processName
	 */
	public EventExecutorBuilder(String processName) {
		this.processName = processName;
	}

	/**
	 * Periodic run passed will be used to run periodic tasks. Any periodic events
	 * will be run as by daemon thread pool.
	 * 
	 * @param periodicRun
	 * @return
	 */
	public EventExecutorBuilder daemon(int periodicRun) {
		this.daemonProcess = true;
		this.periodicRun = periodicRun;
		return this;
	}

	/**
	 * delay passed will be used to delay the task.
	 * 
	 * @param delay
	 * @return
	 */
	public EventExecutorBuilder setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	/**
	 * configure thread pool size of the thread factory
	 * 
	 * @param threadPoolSize
	 * @return
	 */
	public EventExecutorBuilder setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
		return this;
	}

	/**
	 * Builder that will take all the configurations into consideration and return
	 * the final EventExecutor object.
	 * 
	 * @return EventExecutor on which tasks can be executed.
	 */
	public AsynchronousEventExecutor buildAsync() {

		AsynchronousEventExecutor eventExecutor = new AsynchronousEventExecutor(processName);

		eventExecutor.setDelay(this.delay);

		eventExecutor.setThreadPoolSize(threadPoolSize);

		if (this.daemonProcess) {
			eventExecutor.setDaeomon(true);
		}

		eventExecutor.setPeriodicRun(this.periodicRun);

		return eventExecutor;
	}

	/**
	 * Builder that will take all the configurations into consideration and return
	 * the final EventExecutor object.
	 * 
	 * @return EventExecutor on which tasks can be executed.
	 */
	public SynchronousEventExecutor buildSync() {

		SynchronousEventExecutor eventExecutor = new SynchronousEventExecutor(processName);

		eventExecutor.setDelay(this.delay);

		eventExecutor.setThreadPoolSize(threadPoolSize);

		return eventExecutor;
	}

}
