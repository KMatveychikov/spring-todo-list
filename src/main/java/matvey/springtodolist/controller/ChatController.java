package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage receivePublicMessage(@Payload ChatMessage message) {
        return message;
    }

    @MessageMapping("/private-message")
    public ChatMessage receivePrivateMessage(@Payload ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }
}
