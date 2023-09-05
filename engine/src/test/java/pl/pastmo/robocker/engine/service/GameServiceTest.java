package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.google.common.primitives.UnsignedInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;
import pl.pastmo.robocker.engine.model.*;
import pl.pastmo.robocker.engine.response.GameInfo;
import pl.pastmo.robocker.engine.response.TankInfo;
import pl.pastmo.robocker.engine.websocket.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    DockerService dockerServiceMock;
    @Mock
    MessageService messageService;
    @Mock
    ShootService shootService;
    private GameService gameService;
    @Captor
    ArgumentCaptor<TankStateMsg> tankMsgCaptor;

    @BeforeEach
    void setUp() {
        gameService = new GameService(dockerServiceMock, messageService, new MoveService(), shootService);
        gameService.getMoveService().setShootService(shootService);

    }

    @AfterEach
    void reset() {
        Tank.resetCounter();
    }

    @Captor
    ArgumentCaptor<String> imageCaptor;

    @Captor
    ArgumentCaptor<String> networkCaptor;

    @Captor
    ArgumentCaptor<String> containerNameCaptor;

    @Captor
    ArgumentCaptor<String> portCaptor;

    @Test
    public void runGame() throws ConfigurationException {
        mockCreateContainer();
        Game game = new Game();
        Player player = new Player(1);
        game.addPlayer(player);

        gameService.runGame(game);

        verify(dockerServiceMock)
                .createCotnainer(
                        ArgumentMatchers.eq("robockergame/player"),
                        ArgumentMatchers.eq("robocker-net"),
                        ArgumentMatchers.eq("player-1"),
                        ArgumentMatchers.eq("3000:3000"));
    }

    @Test
    public void runGame_two_players() throws ConfigurationException {
        mockCreateContainer();
        Game game = new Game();
        Player player = new Player(1);
        game.addPlayer(player);
        Player player2 = new Player(2);
        game.addPlayer(player2);

        gameService.runGame(game);

        verify(dockerServiceMock, times(2))
                .createCotnainer(
                        imageCaptor.capture(),
                        networkCaptor.capture(),
                        containerNameCaptor.capture(),
                        portCaptor.capture());

        List<String> images = imageCaptor.getAllValues();
        List<String> networks = networkCaptor.getAllValues();
        List<String> containers = containerNameCaptor.getAllValues();
        List<String> ports = portCaptor.getAllValues();

        assertEquals(Arrays.asList("robockergame/player", "robockergame/player"), images);
        assertEquals(Arrays.asList("robocker-net", "robocker-net"), networks);
        assertEquals(Arrays.asList("player-1", "player-2"), containers);
        assertEquals(Arrays.asList("3000:3000", "3001:3000"), ports);

        assertEquals(UnsignedInteger.valueOf(3000), player.getExternalPort());
        assertEquals(UnsignedInteger.valueOf(3001), player2.getExternalPort());


    }

    @Test
    public void runGame_with_tank() throws ConfigurationException {
        mockCreateContainer();
        Game game = new Game();
        Player player = new Player(1);

        Tank tank = new Tank();

        tank.setX(148.0).setZ(31.0);

        player.addTank(tank);

        game.addPlayer(player);

        gameService.runGame(game);

        verify(dockerServiceMock, times(2))
                .createCotnainer(
                        imageCaptor.capture(),
                        networkCaptor.capture(),
                        containerNameCaptor.capture(),
                        portCaptor.capture());

        List<String> images = imageCaptor.getAllValues();
        List<String> networks = networkCaptor.getAllValues();
        List<String> containers = containerNameCaptor.getAllValues();
        List<String> ports = portCaptor.getAllValues();

        assertEquals(Arrays.asList("robockergame/player", "robockergame/tankbasic"), images);
        assertEquals(Arrays.asList("robocker-net", "robocker-net"), networks);
        assertEquals(Arrays.asList("player-1", "tank-1"), containers);
        assertEquals(Arrays.asList("3000:3000", ":80"), ports);


    }

    @Test
    public void getGameDescription() throws ConfigurationException {
        mockCreateContainer();

        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        Tank tank = new Tank();
        tank.setX(148.0).setZ(31.0);
        player.addTank(tank);
        game.addPlayer(player);


        Player player2 = new Player(gameService.getNewPlayerId());
        game.addPlayer(player2);

        gameService.runGame(game);

        String result = gameService.getGameDescription();

        assertThat(result, containsString("Player"));
        assertThat(result, containsString("externalPort=3000"));
        assertThat(result, containsString("externalPort=3001"));
        assertThat(result, containsString("containerName=player-1"));
        assertThat(result, containsString("robockergame/tankbasic"));


    }

    @Test
    public void getPlayerInfo() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.setColor(new Color(0.0f, 1.0f, 0.0f));
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setZ(31.0);
        player.addTank(tank);
        game.addPlayer(player);

        Player player2 = new Player(gameService.getNewPlayerId());
        player2.addIp("3.3.3.3");
        game.addPlayer(player2);

        gameService.setGame(game);

        GameInfo info = gameService.getPlayerInfo("1.1.1.1");

        assertEquals(info.players.get(0).tanks.size(), 1);
        assertEquals(info.players.get(0).current, true);
        assertEquals(info.players.get(0).color.g, 1.0f);
    }

    @Test
    public void getTankInfo() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.setColor(new Color(0.0f, 1.0f, 0.0f));
        player.addIp("1.1.1.1");
        game.addPlayer(player);

        Player player2 = new Player(gameService.getNewPlayerId());
        player2.addIp("3.3.3.3");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setZ(31.0);
        player2.addTank(tank);

        game.addPlayer(player2);

        gameService.setGame(game);

        TankInfo info = gameService.getTankInfo("2.2.2.2");

        assertEquals(info.id, 1);
        assertEquals(info.playerId, 2);
    }

    @Test
    public void move() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setZ(31.0);
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        TankRequest request = new TankRequest().setTankId(1).setActions(List.of((new Action()).setDistance(1)));
        gameService.move("2.2.2.2", request);

        assertEquals(tank.getSteps().size(), 1);

    }

    @ParameterizedTest
    @MethodSource("actionsProvider")
    public void setAction(List<Action> actions, Tank tank, Double[][] expected) throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        TankRequest request = new TankRequest();
        request.setActions(actions);

        tank.setTankRequest(request);

        LinkedList<Step> steps = tank.getSteps();

        for (int i = 0; i < expected.length; i++) {
            Double[] expect = expected[i];

            Step step = steps.get(i);

            assertEquals(expect[0], step.x, Tank.distanceTolerance);
            assertEquals(expect[1], step.z, Tank.distanceTolerance);
            assertEquals(expect[2], step.angle);
            assertEquals(expect[3], step.howManyTimes);

        }

    }

    static Stream<Arguments> actionsProvider() {
        return Stream.of(
                Arguments.arguments(List.of((new Action()).setDistance(1)), (new Tank()), new Double[][]{{0.1, 0.0, 0d, 10d}}),
                Arguments.arguments(List.of((new Action()).setDistance(1.05)), (new Tank()), new Double[][]{{0.1, 0.0, 0d, 10d}, {0.05, 0.0, 0d, 1d}}),
                Arguments.arguments(List.of((new Action()).setDistance(1.05)), (new Tank()), new Double[][]{{0.1, 0.0, 0d, 10d}, {0.05, 0.0, 0d, 1d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)), (new Tank().setAngle(Math.PI / 2)), new Double[][]{{0.0, 0.1, 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(-1.0)), (new Tank().setAngle(Math.PI / 2)), new Double[][]{{0.0, -0.1, 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)), (new Tank().setAngle(Math.PI)), new Double[][]{{-0.1, 0.0, 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)), (new Tank().setAngle(Math.PI * 1.5)), new Double[][]{{0.0, -0.1, 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)),
                        (new Tank().setAngle(Math.PI / 4)), new Double[][]{{0.1 / Math.sqrt(2), 0.1 / Math.sqrt(2), 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)),
                        (new Tank().setAngle(Math.PI * 3 / 4)), new Double[][]{{-0.1 / Math.sqrt(2), 0.1 / Math.sqrt(2), 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)),
                        (new Tank().setAngle(Math.PI * 5 / 4)), new Double[][]{{-0.1 / Math.sqrt(2), -0.1 / Math.sqrt(2), 0d, 10d}}),
                Arguments.arguments(List.of(new Action().setDistance(1.0)),
                        (new Tank().setAngle(-Math.PI / 4)), new Double[][]{{0.1 / Math.sqrt(2), -0.1 / Math.sqrt(2), 0d, 10d}}),
                Arguments.arguments(List.of((new Action()).setAngle(Math.PI / 3)), (new Tank()), new Double[][]{{0d, 0d, Math.PI / 24, 8d}}),
                Arguments.arguments(List.of((new Action()).setAngle(-Math.PI / 3)), (new Tank()), new Double[][]{{0d, 0d, -Math.PI / 24, 8d}}),
                Arguments.arguments(List.of((new Action()).setAngle(0.1)), (new Tank()), new Double[][]{{0d, 0d, 0.1, 1d}}),
                Arguments.arguments(List.of((new Action()).setAngle(-0.1)), (new Tank()), new Double[][]{{0d, 0d, -0.1, 1d}}),
                Arguments.arguments(List.of(new Action().setAngle(-Math.PI / 4), new Action().setDistance(5)),
                        (new Tank().setAngle(Math.PI / 2)),
                        new Double[][]{{0d, 0d, -Math.PI / 24, 6d}, {0.1d / Math.sqrt(2), 0.1d / Math.sqrt(2), 0d, 50d}})
        );
    }

    @Test
    public void setActionTurret() throws ConfigurationException {

        Action action = new Action();
        action.getTurret().setAngle(Math.PI / 2).setShoot(ShootType.END_OF_ACTION);

        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        Tank tank = new Tank();
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        TankRequest request = new TankRequest();
        request.setActions(List.of(action));

        tank.setTankRequest(request);

        LinkedList<Step> steps = tank.getSteps();

        assertEquals(steps.size(), 1);
        Step step = steps.get(0);

        assertEquals(Math.PI / 2, step.turretAngle, Tank.distanceTolerance);
        assertEquals(0, step.turretVerticalAngle, Tank.distanceTolerance);
        assertEquals(ShootType.END_OF_ACTION, step.shootType);

    }

    @Test
    public void doTick() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        when(shootService.checkDemage(any(), any())).thenReturn(0);

        Tank tank = new Tank();

        tank.setX(0d).setZ(0d).setAngle(0d)
                .setTurret(new Turret());
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        tank.getSteps().add(new Step().setX(0.1).setHowManyTimes(2)
                .setTurretAngle(Turret.rotationSpeed * 2 + 0.2)
                .setTurretVerticalAngle(-Turret.rotationSpeed * 2 - 0.2));
        tank.getSteps().add(new Step().setAngle(0.1).setHowManyTimes(2)
                .setTurretAngle(-Turret.rotationSpeed * 2)
                .setTurretVerticalAngle(Turret.rotationSpeed * 2));

        gameService.doTick();
        assertEquals(0.1, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0.2, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed * 2, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed * 2, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0.1, tank.getAngle(), 0.001);
        assertEquals(Turret.rotationSpeed, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0, tank.getTurret().getAngle(), 0.001);
        assertEquals(0, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(-Turret.rotationSpeed, tank.getTurret().getAngle(), 0.001);
        assertEquals(Turret.rotationSpeed, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(-Turret.rotationSpeed * 2, tank.getTurret().getAngle(), 0.001);
        assertEquals(Turret.rotationSpeed * 2, tank.getTurret().getAngleVertical(), 0.001);

        verify(shootService, times(6)).processShoots();
        assertEquals(0, tank.getSteps().size());

    }

    @Test
    public void doTick_withShootOnEnd() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        when(shootService.checkDemage(any(), any())).thenReturn(0);

        Tank tank = new Tank();

        tank.setX(0d).setZ(0d).setAngle(0d).setTurret(new Turret());
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        tank.getSteps().add(new Step().setX(0.1).setHowManyTimes(2).setTurretAngle(Turret.rotationSpeed * 4)
                .setTurretVerticalAngle(-Turret.rotationSpeed * 4).setShootType(ShootType.END_OF_ACTION));
        tank.getSteps().add(new Step().setAngle(0.1).setHowManyTimes(1));

        gameService.doTick();
        assertEquals(0.1, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0.2, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed * 2, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed * 2, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0.2, tank.getX(), 0.001);
        assertEquals(0, tank.getAngle(), 0.001);
        assertEquals(Turret.rotationSpeed * 3, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed * 3, tank.getTurret().getAngleVertical(), 0.001);

        gameService.doTick();
        assertEquals(0.2, tank.getX(), 0.001);
        assertEquals(0, tank.getAngle(), 0.001);
        assertEquals(Turret.rotationSpeed * 4, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed * 4, tank.getTurret().getAngleVertical(), 0.001);

        verify(shootService).shootOnEnd(tank);

        assertEquals(1, tank.getSteps().size());

    }

    @Test
    public void doTick_withShootOnStart() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        when(shootService.checkDemage(any(), any())).thenReturn(0);

        Tank tank = new Tank();

        tank.setX(0d).setZ(0d).setAngle(0d).setTurret(new Turret());
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        tank.getSteps().add(new Step().setX(0.1).setHowManyTimes(2).setTurretAngle(Turret.rotationSpeed * 4)
                .setTurretVerticalAngle(-Turret.rotationSpeed * 2).setShootType(ShootType.NOW));
        tank.getSteps().add(new Step().setAngle(0.1).setHowManyTimes(1));

        gameService.doTick();
        assertEquals(0.1, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed, tank.getTurret().getAngleVertical(), 0.001);
        verify(shootService).shootOnStart(tank);

        gameService.doTick();
        assertEquals(0.2, tank.getX(), 0.001);
        assertEquals(Turret.rotationSpeed * 2, tank.getTurret().getAngle(), 0.001);
        assertEquals(-Turret.rotationSpeed * 2, tank.getTurret().getAngleVertical(), 0.001);
        verify(shootService).shootOnStart(tank);

    }

    @Test
    public void doTick_passing_2PI() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        when(shootService.checkDemage(any(), any())).thenReturn(0);

        Tank tank = new Tank();

        tank.setX(0d).setZ(0d).setAngle(2 * Math.PI - 0.05)
                .setTurret(new Turret());
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        tank.getSteps().add(new Step().setAngle(0.1).setHowManyTimes(1));
        tank.getSteps().add(new Step().setAngle(-0.1).setHowManyTimes(1));

        gameService.doTick();
        assertEquals(0.05, tank.getAngle(), 0.001);

        gameService.doTick();
        assertEquals(2 * Math.PI - 0.05, tank.getAngle(), 0.001);


    }

    @Test
    public void doTick_removing_tank() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        Tank tank = new Tank().setX(0d).setZ(0d).setAngle(2 * Math.PI - 0.05)
                .setTurret(new Turret());

        when(shootService.checkDemage(any(), any())).thenReturn(1);

        player.addTank(tank);
        game.addPlayer(player);


        gameService.setGame(game);
        assertEquals(3, tank.getLifeLevel());

        gameService.doTick();

        assertEquals(2, tank.getLifeLevel());

        gameService.doTick();
        assertEquals(1, tank.getLifeLevel());

        gameService.doTick();

        verify(messageService, times(3)).sendMessage(tankMsgCaptor.capture());
        TankStateMsg tankMsg = tankMsgCaptor.getAllValues().get(2);

        assertEquals(1,tankMsg.getTanks().size());
        assertEquals(0,tankMsg.getTanks().get(0).getLifeLevel());

        assertEquals(0, tank.getLifeLevel());
        verify(dockerServiceMock).remove("tank-1");
        assertEquals(0, player.getTanks().size());
    }


    private void mockCreateContainer() {
        when(dockerServiceMock.createCotnainer(any(), any(), any(), any()))
                .thenReturn(new CreateContainerResponse());
    }
}
