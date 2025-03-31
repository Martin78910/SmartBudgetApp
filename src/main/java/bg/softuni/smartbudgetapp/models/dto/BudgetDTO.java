package bg.softuni.smartbudgetapp.models.dto;

import bg.softuni.smartbudgetapp.models.CategoryEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BudgetDTO {

    private Long id;

    @NotNull(message = "Category is required")
    private CategoryEnum category;

    @Min(value = 0, message = "Monthly limit cannot be negative")
    private Double monthlyLimit;

    @NotNull(message = "Account is required")
    private Long accountId;

    public BudgetDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}


