package pl.pastmo.robocker.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.response.GameInfo;
import pl.pastmo.robocker.engine.service.GameService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InfoController {
    @Autowired
    private GameService gameService;


    @RequestMapping(value = "/info/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GameInfo getMyContainers(HttpServletRequest request) {
        return gameService.getPlayerInfo(request.getRemoteAddr());
    }
}
