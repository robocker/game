package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.AbstractTank;
import pl.pastmo.robocker.engine.model.Tank;

@Component("containerisedTankService")
public class ContainerisedTankService extends AbstractTankService {
    @Autowired
    private DockerService dockerService;

    @Override
    public void fillTankInfo(AbstractTank tank) {

        Tank cTank = (Tank) tank;
        CreateContainerResponse tankResp = dockerService.createCotnainer(cTank.getImageName(), GameService.defaultNetwork, cTank.getContainerName(), dockerService.calculatePorts(cTank));
        dockerService.fillContainerInfo(tankResp.getId(), cTank);

    }

    @Override
    void remove(AbstractTank tank) {
        dockerService.remove(((Tank) tank).getContainerName());

    }
}
