package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.Containerized;

import java.util.List;
import java.util.Map;

@Component("dockerService")
public class DockerService {

    private DockerClient dockerClient;
    private Network currentNetwork;

    public DockerService() {
        dockerClient = DockerClientBuilder.getInstance().build();
    }

    public String getContainers() {

        List<Container> containers = dockerClient.listContainersCmd().exec();

        String result = "";

        for (Container container : containers) {

            result += "Image: " + container.getImage();

            Map<String, ContainerNetwork> network = container.getNetworkSettings().getNetworks();

            String[] names = container.getNames();

            for (String name : names) {
                result += name;
            }

            System.out.println(network.keySet());

            for (String key : network.keySet()) {
                ContainerNetwork containerNetwork = network.get(key);
                System.out.println(containerNetwork.getAliases());
                System.out.println(containerNetwork.getIpAddress());

                result += " ip:" + containerNetwork.getIpAddress();

            }
        }

        return result;
    }

    public void fillContainerInfo(String id, Containerized containerized) {
        InspectContainerResponse container = dockerClient.inspectContainerCmd(id).exec();

        Map<String, ContainerNetwork> networks = container.getNetworkSettings().getNetworks();

        for (String key : networks.keySet()) {
            ContainerNetwork containerNetwork = networks.get(key);

            containerized.addIp(containerNetwork.getIpAddress());
        }
    }

    public CreateContainerResponse createCotnainer(String imageName, String networkName, String containerName,
            String port) {

        PortBinding portBinding = PortBinding.parse(port);

        HostConfig hostConfig = HostConfig
                .newHostConfig()
                .withAutoRemove(true)
                .withPortBindings(portBinding);

        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(imageName)
                .withPortSpecs(port)
                .withName(containerName)
                .withHostConfig(hostConfig)
                .exec();

        connectToNetwork(networkName, containerResponse.getId());

        dockerClient.startContainerCmd(containerResponse.getId()).exec();

        return containerResponse;

    }

    public void connectToNetwork(String networkName, String containerId) {
        this.createNetworkIfNotExist(networkName);
        dockerClient.connectToNetworkCmd().withNetworkId(this.currentNetwork.getId()).withContainerId(containerId)
                .exec();
    }

    public void remove(String containerName) {
        Runnable r1 = () -> {
            dockerClient.stopContainerCmd(containerName).exec();
        };

        new Thread(r1).start();

    }

    public void createNetworkIfNotExist(String name) {

        Network exist = this.getNetwork(name);

        if (exist == null) {

            dockerClient.createNetworkCmd()
                    .withName(name)
                    .withDriver("bridge").exec();

            exist = this.getNetwork(name);

        }

        this.currentNetwork = exist;

    }

    public Network getNetwork(String name) {
        List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(name).exec();

        if (networks.size() > 0) {
            return networks.get(0);
        }

        return null;
    }

}
