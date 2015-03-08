package org.rency.trigger.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeTaskQueueTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(ChangeTaskQueueTrigger.class);

	public void execute(){
		String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		logger.info("at "+ dateTime +" running ChangeTaskQueueTrigger...");
	}
}