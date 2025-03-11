package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {


    // Извлича всички AccountEntity, чийто owner.id съвпада с ownerId
    List<AccountEntity> findAllByOwnerId(Long ownerId);

}
