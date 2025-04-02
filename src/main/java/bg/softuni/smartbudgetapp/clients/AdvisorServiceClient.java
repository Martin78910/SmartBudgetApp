package bg.softuni.smartbudgetapp.clients;


import bg.softuni.smartbudgetapp.models.dto.AdviceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "advisor-service", url = "${advisor.service.url}", fallback = AdvisorServiceClientFallback.class)
public interface AdvisorServiceClient {


    @GetMapping
    String getGenericAdvice();

    @PostMapping
    String createPersonalizedAdvice(@RequestBody AdviceDTO adviceDTO);

}
