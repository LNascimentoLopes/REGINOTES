package LNascimento.Note_Taking_app.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<String> checkAPI() {
        // Retorna um status 200 OK e um JSON simples confirmando que a máquina está ligada
        return ResponseEntity.ok("{\"status\": \"UP\", \"message\": \"Máquina ligada e pronta para o recrutador!\"}");
    }
}