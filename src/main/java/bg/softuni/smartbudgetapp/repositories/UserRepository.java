package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
