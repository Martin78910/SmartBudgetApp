package bg.softuni.smartbudgetapp.repositories;

import bg.softuni.smartbudgetapp.models.CategoryEnum;
import bg.softuni.smartbudgetapp.models.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // Връща сумата на всички outbound транзакции (income=false) за userId + category
    @Query("SELECT SUM(t.amount) " +
            "FROM TransactionEntity t " +
            "WHERE t.account.owner.id = :userId " +
            "  AND t.category = :category " +
            "  AND t.income = false")
    Double getTotalSpendingForCategory(@Param("userId") Long userId,
                                       @Param("category") CategoryEnum category);


    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionEntity t " +
            "WHERE t.account.id = :accountId AND t.category = :category AND t.income = false")
    Double getTotalExpensesByAccountAndCategory(@Param("accountId") Long accountId,
                                                @Param("category") CategoryEnum category);


}
