package pl.pastmo.robocker.engine.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class DockerService {

    private DockerClient dockerClient;

    public void DockerSevice(){
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }

    public String getContainers(){

        List<Container> containers = dockerClient.listContainersCmd().exec();

        String result = "";

        for (Container container: containers) {
            result+= "Image: "+ container.getImage()+" "+container.toString();
        }

        return result;
    }


}
