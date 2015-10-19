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

import javax.annotation.PostConstruct;

@Component
public class EventStream implements CommandLineRunner{

    private static Logger LOG = LoggerFactory.getLogger(EventStream.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    RegistryManager registryManager;

    @Override
    public void run(String... strings) throws Exception {

        for (String beanName : context.getBeanDefinitionNames()) {
            LOG.info("Bean:{}", beanName);
        }
    }

    @PostConstruct
    private void init() {
        EventController.getStream().subscribe(string -> {
            LOG.info("GOTCHA:{}", string);
            registryManager.put(string);
        });
    }

}

