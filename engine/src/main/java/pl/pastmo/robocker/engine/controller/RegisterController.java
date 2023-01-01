package pl.pastmo.robocker.engine.controller;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;
import pl.pastmo.robocker.engine.model.*;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;

@RestController
public class RegisterController {
    @Autowired
    private DockerService dockerService;
    @Autowired
    private GameService gameService;


    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/containers")
    public String containers() {

        DockerService service = new DockerService();
        return service.getContainers();
    }

    @RequestMapping("/containers/create")
    public String create() {

        String randNumber = Math.round(Math.random() * 30) + "";
        CreateContainerResponse result = dockerService.createCotnainer("robocker/player:latest", "robocker_net", "play" + randNumber, ":3000");

        return result.getId();
    }

    @RequestMapping("/containers/demo")
    public String demo() throws ConfigurationException {

        Game game = new Game();

        Player player = new Player(this.gameService.getNewPlayerId()).setColor(new Color(1.0f, 0.0f, 0.0f));
        player.addTank((new Tank()).setX(105.0).setY(41.0).setAngle(Math.PI / 2).setTurret(new Turret()));
        player.addTank((new Tank()).setX(115.0).setY(41.0).setTurret(new Turret()));
//        player.addTank((new Tank()).setX(125.0).setY(41.0));
//        player.addTank((new Tank()).setX(135.0).setY(41.0));
//        player.addTank((new Tank()).setX(145.0).setY(41.0));
//        player.addTank((new Tank()).setX(155.0).setY(41.0));
//        player.addTank((new Tank()).setX(165.0).setY(41.0));
//        player.addTank((new Tank()).setX(175.0).setY(41.0));
//        player.addTank((new Tank()).setX(185.0).setY(41.0));
//        player.addTank((new Tank()).setX(105.0).setY(35.0));
//        player.addTank((new Tank()).setX(115.0).setY(35.0));
//        player.addTank((new Tank()).setX(125.0).setY(35.0));
//        player.addTank((new Tank()).setX(135.0).setY(35.0));
//        player.addTank((new Tank()).setX(145.0).setY(35.0));
//        player.addTank((new Tank()).setX(155.0).setY(35.0));
//        player.addTank((new Tank()).setX(165.0).setY(35.0));
//        player.addTank((new Tank()).setX(175.0).setY(35.0));
//        player.addTank((new Tank()).setX(185.0).setY(35.0));
        game.addPlayer(player);

        Player player2 = new Player(this.gameService.getNewPlayerId()).setColor(new Color(0.0f, 0.0f, 1.0f));
        Tank tank2 = new Tank();
        tank2.setX(120.0).setY(55.0).setAngle(Math.PI / 4).setTurret(new Turret());
        player.addTank(tank2);
        game.addPlayer(player2);

        gameService.runGame(game);

        return gameService.getGameDescription();
    }
}
