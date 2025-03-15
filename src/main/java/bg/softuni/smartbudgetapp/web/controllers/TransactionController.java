package bg.softuni.smartbudgetapp.web.controllers;


import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.AccountDTO;
import bg.softuni.smartbudgetapp.models.dto.TransactionDTO;
import bg.softuni.smartbudgetapp.services.AccountService;
import bg.softuni.smartbudgetapp.services.CategoryService;
import bg.softuni.smartbudgetapp.services.TransactionService;
import bg.softuni.smartbudgetapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService, AccountService accountService, UserService userService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.userService = userService;
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

        // 2) Зареждаме списък от AccountDTO за падащото меню
        List<AccountDTO> userAccounts = accountService.getAllAccountsForUser(user.getId());
        model.addAttribute("accounts", userAccounts);

        // 3) Зареждаме транзакциите, ако accountId != null, иначе празен списък
        List<TransactionDTO> transactions;
        TransactionDTO transactionDTO = new TransactionDTO(); // празен DTO за формата

        if (accountId != null) {
            transactions = transactionService.getTransactionsByAccountId(accountId);
            // по подразбиране да се избере тази сметка в dropdown
            transactionDTO.setAccountId(accountId);
            model.addAttribute("selectedAccountId", accountId);
        } else {
            transactions = List.of();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("transactionDTO", transactionDTO);

        // категориите dropdown:
        model.addAttribute("allCategories", categoryService.getAllCategories());

        return "transactions";
    }

    @PostMapping("/add")
    public String addTransaction(
            @Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            // В случай на грешка – презареждаме списъка сметки и транзакции
            String email = principal.getName();
            UserEntity user = userService.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("Logged user not found!");
            }
            List<AccountDTO> userAccounts = accountService.getAllAccountsForUser(user.getId());
            model.addAttribute("accounts", userAccounts);

            if (transactionDTO.getAccountId() != null) {
                model.addAttribute("transactions",
                        transactionService.getTransactionsByAccountId(transactionDTO.getAccountId()));
                model.addAttribute("selectedAccountId", transactionDTO.getAccountId());
            } else {
                model.addAttribute("transactions", List.of());
            }
                // списък с категориите
             model.addAttribute("allCategories", categoryService.getAllCategories());
            return "transactions";
        }

        // 2) Добавяме транзакцията
        transactionService.addTransaction(transactionDTO);

        // 3) Редирект към GET, за да покажем отново списъка
        if (transactionDTO.getAccountId() == null) {
            // ако  accountId липсва, върни се към /users/transactions
            return "redirect:/users/transactions";
        } else {
            return "redirect:/users/transactions?accountId=" + transactionDTO.getAccountId();
        }
    }
}
