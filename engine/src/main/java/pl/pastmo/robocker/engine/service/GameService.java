package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.google.common.primitives.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.*;
import pl.pastmo.robocker.engine.response.GameInfo;
import pl.pastmo.robocker.engine.response.PlayerInfo;
import pl.pastmo.robocker.engine.response.TankInfo;
import pl.pastmo.robocker.engine.websocket.TankMsg;
import pl.pastmo.robocker.engine.websocket.TankRequest;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;

import javax.annotation.PostConstruct;
import java.util.*;

@Component("gameService")
public class GameService extends TimerTask {

    @Autowired
    private DockerService dockerService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MoveService moveService;
    @Autowired
    private ShootService shootService;

    public static final String defaultNetwork = "robocker-net";
    private Set<UnsignedInteger> usedPorts = new TreeSet<>();
    private Game game;
    private Integer currentPlayerId = 0;

    public GameService() {
    }

    public GameService(DockerService ds, MessageService messageService, MoveService moveService, ShootService shootService) {
        this.dockerService = ds;
        this.messageService = messageService;
        this.moveService = moveService;
        this.shootService = shootService;
    }

    @PostConstruct
    public void init() {
        this.messageService.setGameService(this);
    }


    public void runGame(Game newGame) {
        setGame(newGame);

        dockerService.connectToNetwork(defaultNetwork,"engine");

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
        GameInfo result = new GameInfo();

        for (Player player : game.getPlayers()) {
            PlayerInfo playerResult = new PlayerInfo();

            playerResult.color = player.getColor();
            playerResult.tanks = player.getTanks();
            playerResult.id = player.getId();

            if (player.getIps().contains(ip)) {
                playerResult.current = true;
            }
            result.players.add(playerResult);
        }

        return result;
    }

    public TankInfo getTankInfo(String ip) {
        System.out.println("GameService.getTankInfo ip:" + ip);
        TankInfo result = new TankInfo();

        if (game == null) {
            return result;
        }

        for (Player player : game.getPlayers()) {

            for (Tank tank : player.getTanks()) {
                if (tank.getIps().contains(ip)) {
                    result.id = tank.getId();
                    result.playerId = player.getId();
                    return result;
                }
            }

        }

        return result;
    }

    public void move(String ip, TankRequest request) {
        for (Player player : game.getPlayers()) {
            for (Tank tank : player.getTanks()) {
                if (tank.getId() == request.getTankId()) {
                    System.out.println(tank.getIps());
                    tank.setTankRequest(request);
                }
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

        shootService.processShoots();
        List<Explosion> explosions = shootService.getExplosions();

        tanksMsgs.setExplosions(explosions);
        tanksMsgs.setBullets(shootService.getBullets());

        List<Tank> tanksToRemove = new LinkedList<>();

        for (Player player : game.getPlayers()) {
            for (Tank tank : player.getTanks()) {
                boolean toRemove = false;

                int hits = shootService.checkDemage(tank, explosions);
                tank.decreaseLiveLevel(hits);

                if (tank.getLifeLevel() > 0) {
                    moveService.updatePosition(tank);
                } else {
                    dockerService.remove(tank.getContainerName());
                    tanksToRemove.add(tank);
                    toRemove = true;
                }

                tanksMsgs.add(TankMsg.fromTank(tank, player));
                if (toRemove) {
                    player.getTanks().remove(tank);
                }
            }
        }


        this.messageService.sendMessage(tanksMsgs);

        shootService.removeExplosions(explosions);
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

    public MoveService getMoveService() {
        return moveService;
    }
}
