package pl.pastmo.robocker.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.request.Move;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;
import pl.pastmo.robocker.engine.service.MessageService;
import pl.pastmo.robocker.engine.websocket.RegisterRequest;
import pl.pastmo.robocker.engine.websocket.Tank;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TankController {
    @Autowired
    private GameService gameService;
    @Autowired
    private DockerService dockerService;
    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "/tank/move", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void getMyContainers(HttpServletRequest request, @RequestBody Move move) {
        System.out.println(move.getX());
        System.out.println(move.getY());
        System.out.println(request.getRemoteAddr());

        gameService.move(request.getRemoteAddr(), move);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Tank greeting(RegisterRequest message) throws Exception {
        Tank tank = new Tank(0, 3.3);
        for(int i = 0; i < 100; i++){
            Thread.sleep(1000);
            tank.setX(i);
            messageService.sendMessage(tank);
        }

        return tank;
    }
}
