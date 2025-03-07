package bg.softuni.smartbudgetapp.services.impl;

import bg.softuni.smartbudgetapp.models.RoleEntity;
import bg.softuni.smartbudgetapp.models.UserEntity;
import bg.softuni.smartbudgetapp.models.dto.UserLoginDTO;
import bg.softuni.smartbudgetapp.repositories.RoleRepository;
import bg.softuni.smartbudgetapp.repositories.UserRepository;
import bg.softuni.smartbudgetapp.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity registerUser(UserEntity user, String role) {
        RoleEntity roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(roleEntity);

        // Кодираме първоначалната парола
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserEntity loginUser(UserLoginDTO userLoginDTO) {
        // 1. Намираме UserEntity по email
        UserEntity user = this.findByEmail(userLoginDTO.getEmail());
        if (user == null) {
            return null; // няма такъв потребител
        }

        // 2. Проверяваме паролата
        boolean matches = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
        if (!matches) {
            return null; // паролата е грешна
        }

        // 3. Успех - връщаме user
        return user;
    }

    @Override
    public void updateUserProfile(UserEntity updatedUser) {
        // 1. Вземаме текущия запис от базата
        UserEntity existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Ъпдейтваме полетата (например име, профилна снимка, валута)
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setCurrency(updatedUser.getCurrency());
        existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

        // 3. Проверка за промяна на паролата
        //    Ако потребителят е подал нова парола (и тя не е празна), кодираме и я задаваме.
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        // 4. Запазваме в базата
        userRepository.save(existingUser);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changeUserRole(Long userId, String roleName) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoleEntity roleEntity = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().clear();
        user.getRoles().add(roleEntity);

        userRepository.save(user);
    }

}
