package bg.softuni.smartbudgetapp.services;

import bg.softuni.smartbudgetapp.models.CategoryEnum;
import bg.softuni.smartbudgetapp.models.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getTransactionsByAccountId(Long accountId);
    TransactionDTO addTransaction(TransactionDTO transactionDTO);
    double getSpentForCategory(Long userId, CategoryEnum category);


}
