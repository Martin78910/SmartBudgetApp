package bg.softuni.smartbudgetapp.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact/send")
    public String sendContactMessage(
            @RequestParam("senderName") String senderName,
            @RequestParam("senderEmail") String senderEmail,
            @RequestParam("message") String message
    ) {
        // Тук може да изпратите имейл, да запишете в база и т.н.
        // За пример - просто ще пренасочим към /contact
        return "redirect:/contact";
    }

}
