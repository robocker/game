package pl.pastmo.robocker.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pastmo.robocker.engine.response.TankInfo;
import pl.pastmo.robocker.engine.service.GameService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TankController {
    @Autowired
    private GameService gameService;


//    @RequestMapping(value = "/tank/move", method = RequestMethod.PATCH,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public void move(HttpServletRequest request, @RequestBody TankRequest move) {
//        System.out.println(request.getRemoteAddr());
//
//        gameService.move(request.getRemoteAddr(), move);
//    }

    @RequestMapping(value = "/tank/info", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TankInfo getInfo(HttpServletRequest request) {

        return gameService.getTankInfo(request.getRemoteAddr());
    }

}
