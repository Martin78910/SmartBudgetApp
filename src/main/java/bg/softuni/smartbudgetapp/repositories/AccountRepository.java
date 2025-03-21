package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {


    // Извлича всички AccountEntity, чийто owner.id съвпада с ownerId
    List<AccountEntity> findAllByOwnerId(Long ownerId);

}
