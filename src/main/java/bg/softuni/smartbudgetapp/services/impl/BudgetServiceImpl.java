package bg.softuni.smartbudgetapp.services.impl;


import bg.softuni.smartbudgetapp.models.BudgetEntity;
import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.BudgetDTO;
import bg.softuni.smartbudgetapp.repositories.BudgetRepository;
import bg.softuni.smartbudgetapp.repositories.UserRepository;
import bg.softuni.smartbudgetapp.services.BudgetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BudgetDTO> getAllBudgetsForUser(Long userId) {
        // 1. Намираме всички BudgetEntity по userId
        //    Ако нямате custom метод, ще вземете всички и филтрирате
        List<BudgetEntity> allBudgets = budgetRepository.findAll();

        return allBudgets.stream()
                .filter(b -> b.getUser() != null && b.getUser().getId().equals(userId))
                .map(this::mapEntityToDTO)
                .toList();
    }

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO, Long userId) {
        // 1. Намираме UserEntity
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Създаваме BudgetEntity
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setCategory(budgetDTO.getCategory());
        budgetEntity.setMonthlyLimit(budgetDTO.getMonthlyLimit());
        budgetEntity.setUser(user);

        BudgetEntity saved = budgetRepository.save(budgetEntity);

        // 3. Връщаме DTO
        return mapEntityToDTO(saved);
    }

    private BudgetDTO mapEntityToDTO(BudgetEntity entity) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setMonthlyLimit(entity.getMonthlyLimit());
        // userId / userEmail не са предвидени в DTO, но може да добавите при нужда
        return dto;
    }


}
