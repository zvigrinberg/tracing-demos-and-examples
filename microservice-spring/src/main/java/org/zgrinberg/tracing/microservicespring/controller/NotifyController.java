package org.zgrinberg.tracing.microservicespring.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zgrinberg.tracing.microservicespring.service.NotificationsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotificationsService notificationsService;

    @GetMapping("{/id}")
    public String receiveNotification(@PathVariable String id)
    {
        log.info("Got request to notify subscribers about car with id=" + id );
        notificationsService.sendNotification(id);
        return "Sent Notification!!";
    }
}
