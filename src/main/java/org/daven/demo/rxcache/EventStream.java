/*
 * Copyright (c) 2015.
 * me@davengeo.com
 */

package org.daven.demo.rxcache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.annotation.PostConstruct;

@Component
public class EventStream implements CommandLineRunner{

    private static Logger LOG = LoggerFactory.getLogger(EventStream.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    CacheStream registryManager;

    @Override
    public void run(String... strings) throws Exception {

        for (String beanName : context.getBeanDefinitionNames()) {
            LOG.info("Bean:{}", beanName);
        }
    }

    @PostConstruct
    private void init() {

//
//        CacheStream.getStream().subscribe(string -> {
//            LOG.info("GOTCHA from Cache:{}", string);
//        });

        Observable<String> observable = CacheStream.getStream().mergeWith(EventController.getStream().doOnEach(notification -> {
            registryManager.put((String) notification.getValue());
        })).asObservable();

        observable.subscribe(s -> {
            LOG.info("Merged:{}", s);
        });
    }

}

