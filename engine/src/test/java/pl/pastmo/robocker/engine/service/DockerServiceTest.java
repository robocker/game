package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.DockerClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.pastmo.robocker.engine.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerServiceTest {

    @Mock
    DockerClient dockerClient;

    DockerService dockerService;

    @BeforeEach
    void setUp() {
        dockerService = new DockerService(dockerClient);

    }

    @AfterEach
    void reset() {
        Tank.resetCounter();
    }

    @Test
    void calculatePorts_internal() {
        Tank tank = new Tank();

        String result = dockerService.calculatePorts(tank);

        assertEquals(":80",result);
    }

    @Test
    void calculatePorts_external() {
        Tank tank = new Tank();

        String result = dockerService.calculatePorts(tank);

        assertEquals(":80",result);
    }
}
