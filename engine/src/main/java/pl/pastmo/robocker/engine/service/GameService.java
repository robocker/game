package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.google.common.primitives.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.*;
import pl.pastmo.robocker.engine.request.*;
import pl.pastmo.robocker.engine.response.GameInfo;
import pl.pastmo.robocker.engine.response.PlayerInfo;
import pl.pastmo.robocker.engine.websocket.TankMsg;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

@Component("gameService")
public class GameService extends TimerTask {

    @Autowired
    private DockerService dockerService;
    @Autowired
    private MessageService messageService;

    public static final String defaultNetwork = "robocker-net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();
    private Game game;
    private Integer currentPlayerId = 0;

    public GameService() {
    }

    public GameService(DockerService ds, MessageService messageService) {

        this.dockerService = ds;
        this.messageService = messageService;
    }


    public void runGame(Game newGame) {
        setGame(newGame);
        for (Player player : game.getPlayers()) {
            CreateContainerResponse playerResp = dockerService.createCotnainer(player.getImageName(), defaultNetwork, player.getContainerName(), calculatePorts(player));
            dockerService.fillContainerInfo(playerResp.getId(), player);

            for (Tank tank : player.getTanks()) {
                CreateContainerResponse tankResp = dockerService.createCotnainer(tank.getImageName(), defaultNetwork, tank.getContainerName(), calculatePorts(tank));
                dockerService.fillContainerInfo(tankResp.getId(), tank);
            }
        }

        this.startGameThread();
    }

    public String getGameDescription() {
        StringBuilder response = new StringBuilder();

        response.append(game.getPlayers());

        return response.toString();
    }

    public GameInfo getPlayerInfo(String ip) {
        System.out.println("GameService.getPlayerInfo ip:" + ip);
        GameInfo result= new GameInfo();

        for (Player player : game.getPlayers()) {
            PlayerInfo playerResult = new PlayerInfo();

            playerResult.color = player.getColor();
            playerResult.tanks = player.getTanks();

            if (player.getIps().contains(ip)) {
                playerResult.current = true;
            }
            result.players.add(playerResult);
        }

        return result;
    }

    public void move(String ip, Move destination) {
        for (Player player : game.getPlayers()) {
            for (Tank tank : player.getTanks()) {
                if (tank.getIps().contains(ip)) {
                    tank.setDestination(destination);
                }
            }
        }
    }

    public void moveAll(Move destination) {
        for (Player player : game.getPlayers()) {
            for (Tank tank : player.getTanks()) {
                tank.setDestination(destination);
            }
        }
    }

    public void startGameThread() {
        Timer timer = new Timer();

        timer.schedule(this, 1000, 10);
    }

    public void run() {
        doTick();
    }

    public void doTick() {
        TankStateMsg tanksMsgs = new TankStateMsg();

        for (Player player : game.getPlayers()) {
            for (Tank tank : player.getTanks()) {
                tank.updatePosition();
                tanksMsgs.add(TankMsg.fromTank(tank));
            }
        }
        this.messageService.sendMessage(tanksMsgs);
    }

    public String calculatePorts(Containerized item) {
        UnsignedInteger insiderPort = item.getInsidePortNumber();
        String result = ":" + insiderPort;

        if (item.requiredExternalPort()) {
            UnsignedInteger externalPort = insiderPort;
            while (usedPorts.contains(externalPort)) {
                externalPort = externalPort.plus(UnsignedInteger.valueOf(1));
            }
            usedPorts.add(externalPort);

            item.setExternalPort(externalPort);
            result = externalPort.toString() + result;
        }
        return result;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getNewPlayerId() {
        this.currentPlayerId++;
        return this.currentPlayerId;
    }
}
