package com.aftarobot.traffic.backend.data;

import com.googlecode.objectify.ObjectifyService;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by aubreymalabie on 11/9/16.
 */

public class OfyHelper implements ServletContextListener {
    private static final Logger log = Logger.getLogger(OfyHelper.class.getName());

    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user request if no warmup
        // request.
        log.warning("Registering entities using OfyHelper ..........");
        ObjectifyService.register(FCMUserDTO.class);
        ObjectifyService.register(FCMessageDTO.class);
        log.warning("Objects registered");
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}

