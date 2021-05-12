package pl.pastmo.robocker.engine.controller;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.service.DockerService;
import pl.pastmo.robocker.engine.service.GameService;

@RestController
public class RegisterController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/containers")
    public String containers() {

        DockerService service = new DockerService();
        String result = service.getContainers();

        return result;
    }

    @RequestMapping("/containers/create")
    public String create() {

        DockerService service = new DockerService();

        String randNumber = Math.round(Math.random() * 30) + "";
        CreateContainerResponse result = service.createCotnainer("robocker/player:latest", "robocker_net", "play"+randNumber, ":3000");

        return result.getId();
    }

    @RequestMapping("/containers/demo")
    public String demo() {

        DockerService dockerServiceMock = new DockerService();
        GameService gameService = new GameService(dockerServiceMock);

        Game game = new Game();

        Player player = new Player(null);
        Tank tank = new Tank();
        tank.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank);
        game.addPlayer(player);


        Player player2 = new Player(null);
        Tank tank2 = new Tank();
        tank2.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank2);
        game.addPlayer(player2);

        gameService.runGame(game);

        return gameService.getGameDescription();
    }
}
