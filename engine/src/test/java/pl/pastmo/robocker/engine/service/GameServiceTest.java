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
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;
import pl.pastmo.robocker.engine.model.Color;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.request.Move;
import pl.pastmo.robocker.engine.response.GameInfo;
import pl.pastmo.robocker.engine.response.TankInfo;

import java.util.Arrays;
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

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(dockerServiceMock, messageService);

    }

    @AfterEach
    void reset(){
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
                        ArgumentMatchers.eq("robocker/player"),
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

        assertEquals(Arrays.asList("robocker/player", "robocker/player"), images);
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

        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5);

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

        assertEquals(Arrays.asList("robocker/player", "robocker/tankbasic"), images);
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
        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5);
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
        assertThat(result, containsString("robocker/tankbasic"));


    }

    @Test
    public void getPlayerInfo() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.setColor(new Color(0.0f,1.0f,0.0f));
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5);
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
        player.setColor(new Color(0.0f,1.0f,0.0f));
        player.addIp("1.1.1.1");
        game.addPlayer(player);

        Player player2 = new Player(gameService.getNewPlayerId());
        player2.addIp("3.3.3.3");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5);
        player2.addTank(tank);

        game.addPlayer(player2);

        gameService.setGame(game);

        TankInfo info = gameService.getTankInfo("2.2.2.2");

        assertEquals(info.id, 1);
        assertEquals(info.playerId,2);
    }

    @Test
    public void move() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        Move move = new Move(100.0, 200.0);
        gameService.move("2.2.2.2", move);

        assertEquals(tank.getDestination().getX(), 100.0);
        assertEquals(tank.getDestination().getY(), 200.0);

    }

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    public void doTick(double x, double y, double destinationX, double destinationY,
                       double resultX, double resultY) throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());

        Tank tank = new Tank();

        tank.setX(x).setY(y).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        tank.setDestination(new Move(destinationX, destinationY));

        gameService.doTick();

        assertEquals(resultX, tank.getX(), 0.001);
        assertEquals(resultY, tank.getY(), 0.001);

    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                Arguments.arguments(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                Arguments.arguments(0.0, 0.0, 10.0, 0.0, 0.1, 0.0),
                Arguments.arguments(0.0, 0.0, -10.0, 0.0, -0.1, 0.0),
                Arguments.arguments(0.0, 0.0, 0.0, 10.0, 0.0, 0.1),
                Arguments.arguments(0.0, 0.0, 0.0, -10.0, 0.0, -0.1),
                Arguments.arguments(0.0, 0.0, 10.0, 10.0, 0.1 / Math.sqrt(2), 0.1 / Math.sqrt(2)),
                Arguments.arguments(0.0, 0.0, -10.0, 10.0, -0.1 / Math.sqrt(2), 0.1 / Math.sqrt(2)),
                Arguments.arguments(0.0, 0.0, -10.0, -10.0, -0.1 / Math.sqrt(2), -0.1 / Math.sqrt(2)),
                Arguments.arguments(0.0, 0.0, 10.0, -10.0, 0.1 / Math.sqrt(2), -0.1 / Math.sqrt(2)),
                Arguments.arguments(4.5, -2.0, 10.0, 10.0, 4.541, -1.909),
                Arguments.arguments(0.104, 0.104, 0.109, 0.109, 0.109, 0.109),
                Arguments.arguments(-0.104, -0.104, -0.109, -0.109, -0.109, -0.109),
                Arguments.arguments(0.104, 0.104, 0.101, 0.101, 0.101, 0.101),
                Arguments.arguments(-0.104, -0.104, -0.101, -0.101, -0.101, -0.101)
        );
    }

    private void mockCreateContainer() {
        when(dockerServiceMock.createCotnainer(any(), any(), any(), any()))
                .thenReturn(new CreateContainerResponse());
    }
}
