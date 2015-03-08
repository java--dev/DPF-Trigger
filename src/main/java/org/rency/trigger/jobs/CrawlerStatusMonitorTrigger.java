package org.rency.trigger.jobs;

import java.util.Date;
import java.util.List;

import org.rency.crawler.core.CrawlerConfiguration;
import org.rency.crawler.service.CrawlerService;
import org.rency.pushlet.beans.MessageQueue;
import org.rency.pushlet.service.MessageQueueService;
import org.rency.utils.common.CONST;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.tools.PropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rency.dpf.beans.CrawlerConfig;
import com.rency.dpf.service.CrawlerConfigService;

/**
 * @desc 爬虫状态监视器
 * @author T-rency
 * @date 2014年11月26日 上午10:05:22
 */
public class CrawlerStatusMonitorTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerStatusMonitorTrigger.class);

	private CrawlerService crawlerService = SpringContextHolder.getBean(CrawlerService.class);
	private CrawlerConfigService crawlerConfigService = SpringContextHolder.getBean(CrawlerConfigService.class);
	private MessageQueueService messageQueueService = SpringContextHolder.getBean(MessageQueueService.class);
	
	public void execute(){
		CrawlerConfig crawlerConfig = new CrawlerConfig();
		try{
			List<CrawlerConfiguration> crawlerlist = crawlerService.getCrawlerList();
			if(crawlerlist ==null || crawlerlist.size() == 0){
				return;
			}
			for(CrawlerConfiguration cc : crawlerlist){
				Date newTaskDate = cc.getNewTaskDate();//获取线程池中最后一个任务添加的时间
				Date currentDate = new Date();
				long gap = currentDate.getTime() - newTaskDate.getTime();
				long param = new Long(PropUtils.getInteger("crawler.task.date.gap") * 60 * 1000);
				if( param > gap ){//如果最后任务添加的时间<当前时间与自定义间隔时间之差，则不停止线程池
					continue;
				}
				
				//判断线程池是否已经停止，如果没有停止则发出停止线程池事件
				if(!cc.getParserExecutor().isTerminated() && !cc.getSaveExecutor().isTerminated()){
					cc.getParserExecutor().shutdown();
					cc.getSaveExecutor().shutdown();
				}
				
				//如果线程池已经停止，则更新爬虫状态
				if(cc.getParserExecutor().isTerminated() && cc.getSaveExecutor().isTerminated()){
					crawlerConfig.setCrawlerId(cc.getCrawlerId());
					crawlerConfig.setStatus(false);
					crawlerConfigService.updateStatus(crawlerConfig);
					messageQueueService.add(new MessageQueue(CONST.SERVICE_KIND_CRAWLER, cc.getUser(), "服务已停止"));
					logger.debug("crawler status monitor trigger update crawler["+crawlerConfig.toString()+"] status success...");
				}
			}
		}catch(Exception e){
			logger.error("crawler["+crawlerConfig.toString()+"] status monitor trigger error.",e);
			e.printStackTrace();
		}
	}
}