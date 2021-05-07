package pl.pastmo.robocker.engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.model.Game;
import pl.pastmo.robocker.engine.model.Player;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void  runGame() {
        Game game = new Game();
        Player player = new Player(1);
        game.addPlayer(player);

        gameService.runGame(game);

        verify(dockerServiceMock)
                .createCotnainer(
                        ArgumentMatchers.eq("robocker/player"),
                        ArgumentMatchers.eq("robocker_net"),
                        ArgumentMatchers.eq("player_1"),
                        ArgumentMatchers.eq("3000:3000"));
    }
}