package pl.pastmo.robocker.engine.service;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.model.Containerized;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GameService {

    private DockerService dockerService;
    private static final String defaultNetwork = "robocker_net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();
    private Game game;

    GameService(DockerService ds){

        this.dockerService = ds;
    }

    public void runGame(Game game){

        for(Player player: game.getPlayers()){
            dockerService.createCotnainer(player.getImageName(), defaultNetwork, player.getContainerName(), calculatePorts(player));

            for(Tank tank: player.gatTanks()){
                dockerService.createCotnainer(tank.getImageName(), defaultNetwork, tank.getContainerName(), calculatePorts(tank));
            }
        }
    }

    public String calculatePorts(Containerized item){
        UnsignedInteger insiderPort = item.getInsidePortNumber();
        String result = ":" + insiderPort;

        if(item.requiredExternalPort()){
            UnsignedInteger externalPort = insiderPort;
            while (usedPorts.contains(externalPort)){
                externalPort = externalPort.plus(UnsignedInteger.valueOf(1));
            }
            usedPorts.add(externalPort);

            result = externalPort.toString() + result;
        }
        return result;
    }

}
