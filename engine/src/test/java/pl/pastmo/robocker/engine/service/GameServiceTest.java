package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.google.common.primitives.UnsignedInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.request.Move;
import pl.pastmo.robocker.engine.response.PlayerInfo;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    DockerService dockerServiceMock;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(dockerServiceMock);

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
    public void  runGame() throws ConfigurationException {
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
    public void  runGame_two_players() throws ConfigurationException {
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
    public void  runGame_with_tank() throws ConfigurationException {
        mockCreateContainer();
        Game game = new Game();
        Player player = new Player(1);

        Tank tank = new Tank();

        tank.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);

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
    public void  getGameDescription() throws ConfigurationException {
        mockCreateContainer();

        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        Tank tank = new Tank();
        tank.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);
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
    public void  getPlayerInfo() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank);
        game.addPlayer(player);

        Player player2 = new Player(gameService.getNewPlayerId());
        player2.addIp("3.3.3.3");
        game.addPlayer(player2);

        gameService.setGame(game);

        PlayerInfo info = gameService.getPlayerInfo("1.1.1.1");

        assertEquals(info.tanks.size(), 1);
    }

    @Test
    public void  move() throws ConfigurationException {
        Game game = new Game();

        Player player = new Player(gameService.getNewPlayerId());
        player.addIp("1.1.1.1");

        Tank tank = new Tank();
        tank.addIp("2.2.2.2");
        tank.setX(148).setY(31).setWidthX(5).setWidthY(10).setHeight(5);
        player.addTank(tank);
        game.addPlayer(player);

        gameService.setGame(game);

        Move move = new Move(100.0, 200.0);
        gameService.move("2.2.2.2", move);

        assertEquals(tank.getDestination().getX(), 100.0);
        assertEquals(tank.getDestination().getY(), 200.0);

    }
    private void mockCreateContainer(){
        when(dockerServiceMock.createCotnainer(any(),any(),any(),any()))
                .thenReturn(new CreateContainerResponse());
    }
}
