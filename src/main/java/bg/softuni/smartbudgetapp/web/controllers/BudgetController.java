package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.BudgetDTO;
import bg.softuni.smartbudgetapp.repositories.CategoryRepository;
import bg.softuni.smartbudgetapp.services.BudgetService;
import bg.softuni.smartbudgetapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/users/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryRepository categoryRepository; // или CategoryService, ако имате
    private final UserService userService;

    public BudgetController(BudgetService budgetService,
                            CategoryRepository categoryRepository, UserService userService) {
        this.budgetService = budgetService;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }
    @GetMapping
    public String showBudgets(Model model, Principal principal) {
        // 1. currentUserEmail = principal.getName() => връща email
        String currentUserEmail = principal.getName();

        // 2. Намираме UserEntity
        UserEntity user = userService.findByEmail(currentUserEmail);
        if (user == null) {
            throw new RuntimeException("Logged user not found in DB!");
        }

        // 3. Зареждаме списък BudgetDTO за този user
        var budgets = budgetService.getAllBudgetsForUser(user.getId());
        model.addAttribute("budgets", budgets);

        // 4. Празен BudgetDTO + списък категории
        if (!model.containsAttribute("budgetDTO")) {
            model.addAttribute("budgetDTO", new BudgetDTO());
        }
        model.addAttribute("allCategories", categoryRepository.findAll());

        return "budgets";
    }

    @PostMapping("/create")
    public String createBudget(
            @Valid @ModelAttribute("budgetDTO") BudgetDTO budgetDTO,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (bindingResult.hasErrors()) {
            // При грешки, зареждаме пак budgets.html
            String currentUserEmail = principal.getName();
            UserEntity user = userService.findByEmail(currentUserEmail);
            var budgets = budgetService.getAllBudgetsForUser(user.getId());
            model.addAttribute("budgets", budgets);
            model.addAttribute("allCategories", categoryRepository.findAll());
            return "budgets";
        }

        // 1. текущия потребител
        String currentUserEmail = principal.getName();
        UserEntity user = userService.findByEmail(currentUserEmail);
        if (user == null) {
            throw new RuntimeException("Logged user not found in DB!");
        }

        // 2. Създаване
        budgetService.createBudget(budgetDTO, user.getId());

        return "redirect:/users/budgets";
    }


}
