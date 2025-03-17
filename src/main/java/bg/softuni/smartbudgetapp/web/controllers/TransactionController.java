package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.AccountDTO;
import bg.softuni.smartbudgetapp.models.dto.AdviceDTO;
import bg.softuni.smartbudgetapp.models.dto.TransactionDTO;
import bg.softuni.smartbudgetapp.services.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final UserService userService;
    private final BudgetService budgetService;   // за да вземем лимита
    private final RestTemplate restTemplate;     // за да извикаме микросървиса (през localhost:8080/api/advice)

    public TransactionController(TransactionService transactionService,
                                 CategoryService categoryService,
                                 AccountService accountService,
                                 UserService userService,
                                 BudgetService budgetService,
                                 RestTemplate restTemplate) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.userService = userService;
        this.budgetService = budgetService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showTransactions(@RequestParam(name = "accountId", required = false) Long accountId,
                                   Model model,
                                   Principal principal) {

        // 1) Намираме логнатия user
        String email = principal.getName();
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Logged user not found!");
        }

        // 2) Списък от акаунти за падащото меню
        List<AccountDTO> userAccounts = accountService.getAllAccountsForUser(user.getId());
        model.addAttribute("accounts", userAccounts);

        // 3) Ако accountId е подадено, взимаме транзакциите за тази сметка
        List<TransactionDTO> transactions;
        TransactionDTO transactionDTO = new TransactionDTO();

        if (accountId != null) {
            transactions = transactionService.getTransactionsByAccountId(accountId);
            transactionDTO.setAccountId(accountId);
            model.addAttribute("selectedAccountId", accountId);
        } else {
            transactions = List.of();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("transactionDTO", transactionDTO);

        // dropdown за категориите
        model.addAttribute("allCategories", categoryService.getAllCategories());

        return "transactions";
    }

    @PostMapping("/add")
    public String addTransaction(
            @Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            return "transactions";
        }

        // 1) Добавяме транзакцията
        TransactionDTO savedTx = transactionService.addTransaction(transactionDTO);

        // 2) Намираме логнатия потребител (за userId)
        String email = principal.getName();
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Logged user not found!");
        }
        Long userId = user.getId();

        // 3) Ако е разход (income=false), проверяваме лимита
        if (!savedTx.isIncome()) {
            // колко е лимитът за тази категория
            double monthlyLimit = budgetService.getMonthlyLimit(userId, savedTx.getCategory());
            // колко похарчено до момента (включително новата транзакция)
            double spentSoFar = transactionService.getSpentForCategory(userId, savedTx.getCategory());

            // Ако сме над 90% от лимита (или самия лимит) => викаме микросървиса
            if (monthlyLimit > 0 && spentSoFar >= monthlyLimit * 0.9) {
                // подготвяме AdviceDTO
                AdviceDTO adviceDTO = new AdviceDTO();
                adviceDTO.setUserEmail(user.getEmail());
                adviceDTO.setCurrentSpending(spentSoFar);
                adviceDTO.setBudgetLimit(monthlyLimit);

                // Викаме /api/advice в същото приложение, което от своя страна говори с микросървиса
                String adviceMessage = restTemplate.postForObject(
                        "http://localhost:8080/api/advice",
                        adviceDTO,
                        String.class
                );

                // Слагаме го като flash-attribute, за да се появи след redirect
                redirectAttrs.addFlashAttribute("adviceMessage", adviceMessage);
            }
        }

        // 4) Редиректваме към GET метода, за да не зареждаме ръчно списъка
        if (savedTx.getAccountId() == null) {
            return "redirect:/users/transactions";
        } else {
            return "redirect:/users/transactions?accountId=" + savedTx.getAccountId();
        }
    }
}
