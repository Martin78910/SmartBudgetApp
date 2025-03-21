package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {
}
