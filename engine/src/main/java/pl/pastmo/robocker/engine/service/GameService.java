package pl.pastmo.robocker.engine.service;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.model.*;

import java.util.Set;
import java.util.TreeSet;

public class GameService {

    private DockerService dockerService;
    private static final String defaultNetwork = "robocker-net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();
    private Game game;

    public GameService(DockerService ds){

        this.dockerService = ds;
    }

    public void runGame(Game newGame){
        game = newGame;
        for(Player player: game.getPlayers()){
            dockerService.createCotnainer(player.getImageName(), defaultNetwork, player.getContainerName(), calculatePorts(player));

            for(Tank tank: player.gatTanks()){
                dockerService.createCotnainer(tank.getImageName(), defaultNetwork, tank.getContainerName(), calculatePorts(tank));
            }
        }
    }

    public String getGameDescription(){
        StringBuilder response = new StringBuilder();

        response.append(game.getPlayers());

        return response.toString();
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

            item.setExternalPort(externalPort);
            result = externalPort.toString() + result;
        }
        return result;
    }

}
