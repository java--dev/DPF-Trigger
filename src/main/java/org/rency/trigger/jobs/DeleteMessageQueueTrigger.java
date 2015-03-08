package org.rency.trigger.jobs;

import org.rency.pushlet.service.MessageQueueService;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.tools.PropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 删除消息队列中超过规定期限的消息
 * @author T-rency
 * @date 2014年11月26日 上午10:05:22
 */
public class DeleteMessageQueueTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(DeleteMessageQueueTrigger.class);

	private MessageQueueService messageQueueService = SpringContextHolder.getBean(MessageQueueService.class);
	
	public void execute(){
		try{
			int exceedDay = PropUtils.getInteger("dwr.push.message.queue.exceed.day");
			logger.info("exec delete message queue when createTime exceed "+exceedDay+" day start.");
			int count = messageQueueService.deleteExceedMessage(exceedDay);
			logger.info("exec delete message queue when createTime exceed "+exceedDay+" day finish, and delete "+count+" record.");
		}catch(Exception e){
			logger.error("delete message queue when createTime exceed some day error.",e);
			e.printStackTrace();
		}
	}
}