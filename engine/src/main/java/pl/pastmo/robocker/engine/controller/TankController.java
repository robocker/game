package pl.pastmo.robocker.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.request.Move;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;
import pl.pastmo.robocker.engine.service.MessageService;

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
    public void move(HttpServletRequest request, @RequestBody Move move) {
        System.out.println(move.getX());
        System.out.println(move.getY());
        System.out.println(request.getRemoteAddr());

        gameService.move(request.getRemoteAddr(), move);
    }

    @RequestMapping(value = "/tank/id", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getId() {


       return 42;
    }

}
