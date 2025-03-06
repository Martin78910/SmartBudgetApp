package bg.softuni.smartbudgetapp.web.controllers;

import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.UserRegisterDTO;
import bg.softuni.smartbudgetapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET: /users/register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Ако липсва атрибут userRegisterDTO, създаваме нов
        if (!model.containsAttribute("userRegisterDTO")) {
            model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        }
        return "register";
    }

    // POST: /users/register
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO,
            BindingResult bindingResult,
            Model model) {

        // Ако има грешки при валидацията, връщаме същата страница
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Тук може да проверите дали password и confirmPassword съвпадат и т.н.
        // Преобразуваме DTO към UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(userRegisterDTO.getPassword());
        userEntity.setFullName(userRegisterDTO.getFullName());

        // Извикваме сървиса, за да запишем в базата
        userService.registerUser(userEntity, "ROLE_USER");

        // Пренасочваме към /login (или където желаете)
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        // TODO: зареждате потребител, добавяте в model
        return "profiles";
    }

    @PostMapping("/profile/update")
    public String updateProfile(/* DTO или @ModelAttribute */) {
        // TODO: логика за update
        return "redirect:/users/profile";
    }



}
