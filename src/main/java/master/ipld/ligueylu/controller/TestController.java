package master.ipld.ligueylu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/test-home")
public class TestController {
    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> sayHelloClient() {
        return ResponseEntity.ok("Hello World from secured endpoint by client");
    }

    @GetMapping("/prestataire")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<String> sayHelloPrestataire() {
        return ResponseEntity.ok("Hello World from secured endpoint by prestataire");
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sayHelloAdmin() {
        return ResponseEntity.ok("Hello World from secured endpoint by admin");
    }
}
