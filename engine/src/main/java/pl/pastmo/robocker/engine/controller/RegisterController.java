package pl.pastmo.robocker.engine.controller;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        CreateContainerResponse result = dockerService.createCotnainer("robockergame/player:latest", "robocker_net", "play" + randNumber, ":3000");

        return result.getId();
    }

    @RequestMapping("/containers/demo")
    public String demo(@RequestParam(value = "tanks", required = false) String[] parameters) throws ConfigurationException {

        Game game = new Game();

        if (parameters.length == 0) {
            parameters = new String[]{TankRemote.REMOTE, TankRemote.REMOTE};
        }

        for (String tankImage : parameters) {
            boolean isRemote = tankImage.equals(TankRemote.REMOTE);

            Player player = new Player(this.gameService.getNewPlayerId()).setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random())).setRemote(isRemote);

            Double basePosition = Math.random() * 100;

            if(isRemote){
                System.out.println("remote");
                player.addTank((new TankRemote()).setX(basePosition).setZ(basePosition).setAngle(Math.PI / 2).setTurret(new Turret()));
                player.addTank((new TankRemote()).setX(basePosition + 10).setZ(basePosition).setTurret(new Turret()));
            }
            else{
                System.out.println("docker");
                player.addTank((new Tank(tankImage)).setX(basePosition).setZ(basePosition).setAngle(Math.PI / 2).setTurret(new Turret()));
                player.addTank((new Tank(tankImage)).setX(basePosition + 10).setZ(basePosition).setTurret(new Turret()));
            }


            game.addPlayer(player);

        }

        gameService.runGame(game);

        return gameService.getGameDescription();

    }
}
