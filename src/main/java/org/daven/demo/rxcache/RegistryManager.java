/*
 * Copyright (c) 2015.
 * me@davengeo.com
 */

package org.daven.demo.rxcache;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RegistryManager {

    private static Logger LOG = LoggerFactory.getLogger(RegistryManager.class);

    Cache<String, String> cache;

    @PostConstruct
    public void init() {
        DefaultCacheManager defaultCacheManager = new DefaultCacheManager();
        defaultCacheManager.start();
        cache = defaultCacheManager.getCache("registry-1", true);
        cache.addListener(new PrintWhenAdded());
    }

    public void put(String value) {
        cache.put("one", value);
    }

    @Listener
    public class PrintWhenAdded {

        @CacheEntryModified
        public void modified(CacheEntryModifiedEvent event) {
            LOG.info("modified{}", event.getValue());
        }

    }

}
