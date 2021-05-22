package pl.pastmo.robocker.engine.controller;

import com.github.dockerjava.api.model.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.response.PlayerInfo;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private GameService gameService;
    @Autowired
    private DockerService dockerService;


    @RequestMapping(value = "/info/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PlayerInfo getMyContainers(HttpServletRequest request) {
        return gameService.getPlayerInfo(request.getRemoteAddr());
    }
}
