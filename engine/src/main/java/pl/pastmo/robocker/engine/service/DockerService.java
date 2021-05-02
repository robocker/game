package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class DockerService {

    private DockerClient dockerClient;
    private Network currentNetwork;


    public void DockerService(){


    }

    public String getContainers(){

        dockerClient = DockerClientBuilder.getInstance().build();

        List<Container> containers = dockerClient.listContainersCmd().exec();

        String result = "";

        for (Container container: containers) {
            result+= "Image: "+ container.getImage()+" "+container.toString();
        }

        return result;
    }

   public CreateContainerResponse createCotnainer(String imageName, String networkName, String containerName, String port){

        dockerClient = DockerClientBuilder.getInstance().build();

        PortBinding portBinding = PortBinding.parse(port);

       HostConfig hostConfig = HostConfig
               .newHostConfig()
               .withAutoRemove(true)
               .withPortBindings(portBinding);


        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withPortSpecs(port)
                .withName(containerName)
                .withHostConfig(hostConfig)
                .exec();


       this.createNetworkIfNotExist(networkName);

       dockerClient.connectToNetworkCmd().withNetworkId(this.currentNetwork.getId()).withContainerId(container.getId()).exec();

       dockerClient.startContainerCmd(container.getId()).exec();

       return container;

//        String result = "";
//
//        for (Container container: containers) {
//            result+= "Image: "+ container.getImage()+" "+container.toString();
//        }
//
//        return result;
    }

   public void createNetworkIfNotExist(String name){
        dockerClient = DockerClientBuilder.getInstance().build();

     Network exist = this.getNetwork(name);

       if(exist == null){

           dockerClient.createNetworkCmd()
                   .withName(name)
                   .withDriver("bridge").exec();

           exist = this.getNetwork(name);

       }

       this.currentNetwork = exist;

    }

    private Network getNetwork(String name){
        List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(name).exec();

        if(networks.size() > 0){
            return networks.get(0);
        }

        return null;
    }


}
