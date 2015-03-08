package org.rency.trigger.jobs;

import org.rency.lucene.core.LuceneConfiguration;
import org.rency.lucene.service.LuceneService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 定时更新Lucene索引
 * @author T-rency
 * @date 2015年1月4日 下午5:11:05
 */
public class LuceneIndexTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(LuceneIndexTrigger.class);
	private LuceneService luceneService = SpringContextHolder.getBean(LuceneService.class);

	public void execute(){
		logger.info("run create lucene index trigger at "+Utils.getNowDateTime());
		try{
			LuceneConfiguration cfg = new LuceneConfiguration("trigger",LuceneDict.LUCENE_INDEX_STORE_FILE,LuceneDict.INDEX_PATH);
			luceneService.startIndex(cfg);
		}catch(Exception e){
			logger.error("trigger occur exception when run create lucene index.",e);
			e.printStackTrace();
		}
		logger.info("run create lucene index trigger finish at "+Utils.getNowDateTime());
	}
}