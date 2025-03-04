package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
