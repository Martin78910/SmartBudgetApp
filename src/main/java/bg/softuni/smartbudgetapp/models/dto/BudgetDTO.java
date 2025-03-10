package bg.softuni.smartbudgetapp.models.dto;

import bg.softuni.smartbudgetapp.models.CategoryEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BudgetDTO {

    private Long id;

    @NotNull(message = "Category is required")
    private CategoryEnum category;

    @Min(value = 0, message = "Monthly limit cannot be negative")
    private Double monthlyLimit;

    public BudgetDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "Category is required") CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(@NotNull(message = "Category is required") CategoryEnum category) {
        this.category = category;
    }

    public @Min(value = 0, message = "Monthly limit cannot be negative") Double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(@Min(value = 0, message = "Monthly limit cannot be negative") Double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }
}
