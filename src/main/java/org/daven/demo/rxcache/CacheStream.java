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
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

import javax.annotation.PostConstruct;
import java.util.EventListener;

@Component
public class CacheStream {

    private static EmitListener emitListener;
    private static Observable<String> stream = Observable.
            create((Subscriber<? super String> subscriber) -> {
                register(subscriber::onNext);
            });
    Cache<String, String> cache;

    public static Observable<String> getStream() {
        return stream;
    }

    private static void register(EmitListener listener) {
        emitListener = listener;
    }

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

    private interface EmitListener extends EventListener {
        void emit(String str);
    }

    @Listener
    class PrintWhenAdded {

        @CacheEntryModified
        public void modified(CacheEntryModifiedEvent event) {
            if (!event.isPre()) {
                emitListener.emit((String) event.getValue());
                System.out.println("event:" + event.getValue());
            }
        }
    }
}
