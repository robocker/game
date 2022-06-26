package pl.pastmo.robocker.engine.service;
//import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        System.out.println(session.getRemoteAddress());
        System.out.println(message.getPayload());

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    public void sendMessage(TankStateMsg tankStateMsg) {
        for (WebSocketSession webSocketSession : sessions) {

            if (webSocketSession.isOpen()) {
                String response = new Gson().toJson(tankStateMsg);
                try {
                    webSocketSession.sendMessage(new TextMessage(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
