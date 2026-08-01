package fr.khylick.chronicles.system;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
            "application", "Chronicles",
            "status", "UP",
            "timestamp", Instant.now()
        );
    }
}