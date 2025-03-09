package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.CurrencyEntity;
import bg.softuni.smartbudgetapp.models.CurrencyEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByCode(CurrencyEnum code);

}
