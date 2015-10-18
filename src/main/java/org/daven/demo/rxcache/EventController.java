/*
 * Copyright (c) 2015.
 * me@davengeo.com
 */

package org.daven.demo.rxcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private static Logger LOG = LoggerFactory.getLogger(EventController.class);

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<String> postEvent(
            @RequestParam(value = "event",required = true) String event) {
        LOG.info("received {}", event);
        return ok().body("OK");
    }
}
