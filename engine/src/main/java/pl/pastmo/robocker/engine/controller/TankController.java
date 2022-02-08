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
import pl.pastmo.robocker.engine.websocket.TankState;

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

    @MessageMapping("/tanks")
    @SendTo("/state/tanks")
    public TankState greeting(RegisterRequest message) throws Exception {
        TankState tanks = new TankState();

        tanks.add(new Tank(4,2));
        tanks.add(new Tank(2,8.5));

        return tanks;
    }
}
