package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.models.dto.AccountDTO;
import bg.softuni.smartbudgetapp.models.dto.BudgetDTO;
import bg.softuni.smartbudgetapp.models.dto.TransactionDTO;
import bg.softuni.smartbudgetapp.services.CategoryService;
import bg.softuni.smartbudgetapp.services.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/")
    public String index(Model model) {

        return "index";
    }


    @GetMapping("/users/home")
    public String showHome() {
        return "home";
    }


    @GetMapping("/users/accounts")
    public String showAccounts(Model model) {

        if (!model.containsAttribute("accountDTO")) {
            model.addAttribute("accountDTO", new AccountDTO());
        }

        return "accounts";
    }







    @GetMapping("/users/transactions")
    public String showTransactions(Model model) {

        if (!model.containsAttribute("transactionDTO")) {
            model.addAttribute("transactionDTO", new TransactionDTO());
        }

        model.addAttribute("allCategories", categoryService.getAllCategories());


        return "transactions";
    }



}
