package pl.pastmo.robocker.engine.controller;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;
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
        String result = service.getContainers();

        return result;
    }

    @RequestMapping("/containers/create")
    public String create() {

        String randNumber = Math.round(Math.random() * 30) + "";
        CreateContainerResponse result = dockerService.createCotnainer("robocker/player:latest", "robocker_net", "play"+randNumber, ":3000");

        return result.getId();
    }

    @RequestMapping("/containers/demo")
    public String demo() {

        Game game = new Game();

        Player player = new Player(null);
        Tank tank = new Tank();
        tank.setX(105).setY(41);
        player.addTank(tank);
        game.addPlayer(player);

        Player player2 = new Player(null);
        Tank tank2 = new Tank();
        tank2.setX(391).setY(426);
        player.addTank(tank2);
        game.addPlayer(player2);

        gameService.runGame(game);

        return gameService.getGameDescription();
    }
}
