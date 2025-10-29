package org.galio.uniqloservice.application.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.galio.uniqloservice.application.xxljob.serive.UniqloHandlerService;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class UniqloHandler {

    @Autowired
    private UniqloHandlerService service;

   @XxlJob("favoriteHandler")
    public void favoriteHandler(){
        log.info("查证任务开始。。。。。");
        service.favoriteHandler();
    }
}
