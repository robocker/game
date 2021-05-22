package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.google.common.primitives.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.*;
import pl.pastmo.robocker.engine.response.PlayerInfo;

import java.util.Set;
import java.util.TreeSet;

@Component("gameService")
public class GameService {

    @Autowired
    private DockerService dockerService;
    public static final String defaultNetwork = "robocker-net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();
    private Game game;

    public GameService(){}

    public GameService(DockerService ds){

        this.dockerService = ds;
    }


    public void runGame(Game newGame){
        setGame(newGame);
        for(Player player: game.getPlayers()){
            CreateContainerResponse playerResp = dockerService.createCotnainer(player.getImageName(), defaultNetwork, player.getContainerName(), calculatePorts(player));
            dockerService.fillContainerInfo(playerResp.getId(), player);

            for(Tank tank: player.gatTanks()){
                CreateContainerResponse tankResp =  dockerService.createCotnainer(tank.getImageName(), defaultNetwork, tank.getContainerName(), calculatePorts(tank));
                dockerService.fillContainerInfo(tankResp.getId(), tank);
            }
        }
    }

    public String getGameDescription(){
        StringBuilder response = new StringBuilder();

        response.append(game.getPlayers());

        return response.toString();
    }

    public PlayerInfo getPlayerInfo(String ip){
        System.out.println("Required ip:"+ ip);
        PlayerInfo result = new PlayerInfo();

        for(Player player: game.getPlayers()){
            if(player.getIps().contains(ip)){
                result.tanks = player.gatTanks();
            }
        }

        return result;
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

    public void setGame(Game game){
        this.game = game;
    }

}
