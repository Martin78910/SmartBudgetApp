package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {


}
