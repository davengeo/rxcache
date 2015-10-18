/*
 * Copyright (c) 2015.
 * me@davengeo.com
 */

package org.daven.demo.rxcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.EventListener;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private static Logger LOG = LoggerFactory.getLogger(EventController.class);

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<String> postEvent(
            @RequestParam(value = "event",required = true) String event) {
        LOG.info("received {}", event);
        emitListener.emit(event);
        return ok().body("OK");
    }

    public static Observable<String> getStream() {
        return stream;
    }

    private static Observable<String> stream =
            Observable.create((Subscriber<? super String> subscriber) -> {

        class RestListener implements  EmitListener {

            @Override
            public void emit(String str) {
                subscriber.onNext(str);
            }
        }
        register(new RestListener());
    }).
            subscribeOn(Schedulers.io());

    private static EmitListener emitListener;

    private static void register(EmitListener listener) {
        emitListener = listener;
    }

    interface EmitListener extends EventListener {

        void emit(String str);

    }
}
