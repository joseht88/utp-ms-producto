package upeu.ms.app.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SwaggerController {

    @GetMapping("/swagger-ui")
    public RedirectView index() {
        return new RedirectView ("swagger-ui/index.html");
    }
}
