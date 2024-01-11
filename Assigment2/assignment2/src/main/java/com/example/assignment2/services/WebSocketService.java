package com.example.assignment2.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToClient(String message) {
        // Send a message to the WebSocket topic "/topic/receiveMessage"
        messagingTemplate.convertAndSend("/topic/notification", message);
    }
}

