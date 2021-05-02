package pl.pastmo.robocker.engine.controller;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pastmo.robocker.engine.service.DockerService;

@RestController
public class RegisterController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/containers")
    public String containers() {

        DockerService service = new DockerService();
        String result = service.getContainers();

        return result;
    }

    @RequestMapping("/containers/create")
    public String create() {

        DockerService service = new DockerService();

        String randNumber = Math.round(Math.random() * 30) + "";
        CreateContainerResponse result = service.createCotnainer("robocker/player:latest", "robocker_net", "play"+randNumber, ":3000");

        return result.getId();
    }

    @RequestMapping("/containers/demo")
    public String demo() {

        DockerService service = new DockerService();
        CreateContainerResponse result = service.createCotnainer("robocker/player:latest", "robocker_net", "player", "3000:3000");

        CreateContainerResponse result2 = service.createCotnainer("robocker/tankbasic:latest", "robocker_net", "tank-11","81:80");
        CreateContainerResponse result3 = service.createCotnainer("robocker/tankbasic:latest", "robocker_net", "tank-42",":80");

        String[] response = {result.getId(), result.getId(), result.getId()};
        return response.toString();
    }
}
