package bg.softuni.smartbudgetapp.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BudgetDTO {

    private Long id;

    @NotBlank
    private String category;

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

    public @NotBlank String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank String category) {
        this.category = category;
    }

    public @Min(value = 0, message = "Monthly limit cannot be negative") Double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(@Min(value = 0, message = "Monthly limit cannot be negative") Double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }
}
