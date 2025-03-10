package bg.softuni.smartbudgetapp.services;

import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.UserLoginDTO;

import java.util.List;

public interface UserService {

    UserEntity registerUser(UserEntity user, String role);
    UserEntity findByEmail(String email);
    void updateUserProfile(UserEntity user);
    List<UserEntity> getAllUsers();
    void changeUserRole(Long userId, String roleName);
    UserEntity loginUser(UserLoginDTO userLoginDTO);
}
