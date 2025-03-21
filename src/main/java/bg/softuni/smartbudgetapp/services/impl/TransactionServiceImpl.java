package bg.softuni.smartbudgetapp.services.impl;

import bg.softuni.smartbudgetapp.models.AccountEntity;
import bg.softuni.smartbudgetapp.models.CategoryEnum;
import bg.softuni.smartbudgetapp.models.TransactionEntity;
import bg.softuni.smartbudgetapp.models.dto.TransactionDTO;
import bg.softuni.smartbudgetapp.repositories.AccountRepository;
import bg.softuni.smartbudgetapp.repositories.TransactionRepository;
import bg.softuni.smartbudgetapp.services.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        List<TransactionEntity> transactions = transactionRepository.findAll()
                .stream()
                .filter(t -> t.getAccount() != null && t.getAccount().getId().equals(accountId))
                .collect(Collectors.toList());

        return transactions.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
        AccountEntity account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setDescription(transactionDTO.getDescription());
        transactionEntity.setCategory(transactionDTO.getCategory());
        transactionEntity.setIncome(transactionDTO.isIncome());
        // Ако не е подадена дата, задаваме текущата дата
        transactionEntity.setTransactionDate(transactionDTO.getTransactionDate() != null
                ? transactionDTO.getTransactionDate()
                : LocalDateTime.now());
        transactionEntity.setAccount(account);

        TransactionEntity savedEntity = transactionRepository.save(transactionEntity);
        return mapEntityToDTO(savedEntity);
    }

    private TransactionDTO mapEntityToDTO(TransactionEntity entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setIncome(entity.isIncome());
        dto.setTransactionDate(entity.getTransactionDate());
        if (entity.getAccount() != null) {
            dto.setAccountId(entity.getAccount().getId());
        }
        return dto;
    }


    /**
     * метод, който използва custom метод на TransactionRepository.
     * Ако няма намерени транзакции, SUM(...) ще върне null, затова го обработваме.
     */
    @Override
    public double getTotalSpendingForCategory(Long userId, CategoryEnum category) {
        Double sum = transactionRepository.getTotalSpendingForCategory(userId, category);
        return (sum != null) ? sum : 0.0;
    }



    @Override
    public double getSpentForCategory(Long userId, CategoryEnum category) {
        // взимаме всички транзакции, които са за потребителя (чрез неговите сметки)
        // и имат тази категория, и са outbound (income=false).
        // Имаме AccountEntity(owner=UserEntity). => Нужен ни е userId там.

        // Първо намираме всички транзакции,

        List<TransactionEntity> all = transactionRepository.findAll();
        return all.stream()
                .filter(t -> t.getAccount() != null
                        && t.getAccount().getOwner() != null
                        && t.getAccount().getOwner().getId().equals(userId)
                        && t.getCategory() == category
                        && !t.isIncome() // само разходи
                )
                .mapToDouble(TransactionEntity::getAmount)
                .sum();
    }



}
