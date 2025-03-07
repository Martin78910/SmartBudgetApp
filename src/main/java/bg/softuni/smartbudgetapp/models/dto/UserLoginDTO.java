package bg.softuni.smartbudgetapp.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {


    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UserLoginDTO() {
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
}
