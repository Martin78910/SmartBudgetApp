package bg.softuni.smartbudgetapp.web.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {

        return "index";
    }


 @GetMapping("users/home")
    public String showHome() {
        return "home"; 
    }



}
