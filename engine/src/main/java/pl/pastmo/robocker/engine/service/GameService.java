package pl.pastmo.robocker.engine.service;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.model.Containerized;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GameService {

    private DockerService dockerService;
    private static final String defaultNetwork = "robocker_net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();

    GameService(DockerService ds){

        this.dockerService = ds;
    }

    public void runGame(Game game){

        for(Player player: game.getPlayers()){
            dockerService.createCotnainer(player.getImageName(), defaultNetwork,player.getContainerName(), calculatePorts(player));
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
