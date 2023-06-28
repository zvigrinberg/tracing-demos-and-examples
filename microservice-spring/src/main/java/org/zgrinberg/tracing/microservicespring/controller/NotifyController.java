package org.zgrinberg.tracing.microservicespring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @GetMapping
    public String receiveNotification()
    {
        log.info("hello from notification service");
        return "Got Notification!!";
    }
}
