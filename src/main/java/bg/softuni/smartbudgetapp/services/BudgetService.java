package bg.softuni.smartbudgetapp.services;

import bg.softuni.smartbudgetapp.models.dto.BudgetDTO;

import java.util.List;

public interface BudgetService {

    List<BudgetDTO> getAllBudgetsForUser(Long userId);

    BudgetDTO createBudget(BudgetDTO budgetDTO, Long userId);
}
