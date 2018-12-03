package com.ran.sns.async;

import java.util.List;

public interface EventHandler {
	void doHandler(EventModel model);

	List<EventType> getSupportEventTypes();
}
