package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.clients.AdvisorServiceClient;
import bg.softuni.smartbudgetapp.models.dto.AdviceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/advice")
public class AdviceRestController {

    private final AdvisorServiceClient advisorServiceClient;

    public AdviceRestController(AdvisorServiceClient advisorServiceClient) {
        this.advisorServiceClient = advisorServiceClient;
    }


    @GetMapping
    public ResponseEntity<String> getAdvice() {
        try {
            String response = advisorServiceClient.getGenericAdvice();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("Съветът не е наличен в момента. Опитайте по-късно.");
        }
    }

    @PostMapping
    public ResponseEntity<String> postAdvice(@RequestBody AdviceDTO adviceDTO) {
        try {
            String response = advisorServiceClient.createPersonalizedAdvice(adviceDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("Неуспешно изпращане на съвет. Опитайте по-късно.");
        }
    }


}
