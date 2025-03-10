package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.CategoryEntity;
import bg.softuni.smartbudgetapp.models.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCode(CategoryEnum code);
}
