package bg.softuni.smartbudgetapp.web.controllers;

import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.UserLoginDTO;
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
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String LoginForm(Model model) {
        // Ако в model-а няма userLoginDTO, създаваме нов
        if (!model.containsAttribute("userLoginDTO")) {
            model.addAttribute("userLoginDTO", new UserLoginDTO());
        }
        return "login";
    }


    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
            BindingResult bindingResult,
            Model model) {

        // 1) Валидация на полетата (Email, Password)
        if (bindingResult.hasErrors()) {
            // ако има грешки, връщаме login формата и показваме съобщения
            return "login";
        }

        // 2) Извикваме логиката за логин (примерен метод в сървиса)
        var userEntity = userService.loginUser(userLoginDTO);
        if (userEntity == null) {
            // ако userService върне null (неуспешен логин), добавяме съобщение и се връщаме
            model.addAttribute("badCredentials", true);
            return "login";
        }

        // 3) Успешен логин – може да запишете потребителя в сесия, SecurityContext, да пренасочите и т.н.
        // Тук, ако ползвате Spring Security "out-of-the-box", обикновено не правите manual логин,
        // а разчитате на formLogin(). Но ако е custom логика, покажете профила:
        return "redirect:/users/profile";
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
