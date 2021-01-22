package pl.pastmo.robocker.engine.controller;
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
}
