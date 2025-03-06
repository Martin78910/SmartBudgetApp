package bg.softuni.smartbudgetapp.models.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    private String confirmPassword;

    @Size(min = 3, message = "Full name must be at least 3 characters")
    private String fullName;

    public UserRegisterDTO() {
    }

    public @NotBlank @Email(message = "Please enter a valid email") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "Please enter a valid email") String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password) {
        this.password = password;
    }

    public @NotBlank String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @Size(min = 3, message = "Full name must be at least 3 characters") String getFullName() {
        return fullName;
    }

    public void setFullName(@Size(min = 3, message = "Full name must be at least 3 characters") String fullName) {
        this.fullName = fullName;
    }
}
