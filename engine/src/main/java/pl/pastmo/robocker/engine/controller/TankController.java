package pl.pastmo.robocker.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TankController {
    @Autowired
    private GameService gameService;
    @Autowired
    private DockerService dockerService;


    @RequestMapping(value = "/tank/move", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void getMyContainers(HttpServletRequest request) {
        System.out.println(request.getParameter("body"));
        //, request.getParameters()
//        gameService.move(request.getRemoteAddr(), destination);
    }
}
