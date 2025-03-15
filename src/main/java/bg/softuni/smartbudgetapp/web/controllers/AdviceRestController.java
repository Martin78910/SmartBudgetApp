package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.models.dto.AdviceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/advice")
public class AdviceRestController {

    private final RestTemplate restTemplate;

    @Value("${advisor.service.url}")
    private String advisorServiceUrl;


    public AdviceRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping
    public ResponseEntity<String> getAdvice() {
        // Вика GET endpoint-a на микросървиса
        String response = restTemplate.getForObject(advisorServiceUrl, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> postAdvice(@RequestBody AdviceDTO adviceDTO) {
        // POST заявка към микросървиса
        String response = restTemplate.postForObject(advisorServiceUrl, adviceDTO, String.class);
        return ResponseEntity.ok(response);
    }

}
