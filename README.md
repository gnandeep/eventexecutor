# Event Executor

Event Executor is a Java library for triggering one time events and periodic events.

## Installation

--working on updating jar to maven central repository.



## Usage

```java
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
```


