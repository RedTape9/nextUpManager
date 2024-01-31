package io.github.redtape9.nextupmanager.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller


public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/updates")
    public String send(String message) {
        return message;
    }
}
