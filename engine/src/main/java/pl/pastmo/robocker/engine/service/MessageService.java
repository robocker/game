package pl.pastmo.robocker.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;

@Service
public class MessageService {

    @Autowired
    public SimpMessageSendingOperations messagingTemplate;

    public void sendMessage( TankStateMsg tankStateMsg) {
        messagingTemplate.convertAndSend( "/state/tanks", tankStateMsg);
    }
}
