package eventexecutor;

import java.util.function.Consumer;

import org.eventexecutor.EventExecutorBuilder;

class EventExecutorTest {

	public static void main(String[] args) {
		String message = "Time: ";
		System.out.println(message + System.currentTimeMillis());
		try {
			new EventExecutorBuilder("Test1").setDelay(5).buildAsync().run(message, new Consumer<String>() {

				@Override
				public void accept(String t) {
					System.out.println(message + System.currentTimeMillis());

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
