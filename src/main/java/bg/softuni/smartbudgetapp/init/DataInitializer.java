package bg.softuni.smartbudgetapp.init;

import bg.softuni.smartbudgetapp.models.CurrencyEntity;
import bg.softuni.smartbudgetapp.models.CurrencyEnum;
import bg.softuni.smartbudgetapp.models.RoleEntity;
import bg.softuni.smartbudgetapp.repositories.CurrencyRepository;
import bg.softuni.smartbudgetapp.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CurrencyRepository currencyRepository;

    public DataInitializer(RoleRepository roleRepository,
                           CurrencyRepository currencyRepository) {
        this.roleRepository = roleRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Зареждаме роли (ако не съществуват)
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");

        // 2. Зареждаме валути (ако не съществуват)
        createCurrencyIfNotExists(CurrencyEnum.BGN);
        createCurrencyIfNotExists(CurrencyEnum.EUR);
        createCurrencyIfNotExists(CurrencyEnum.USD);
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });
    }

    private void createCurrencyIfNotExists(CurrencyEnum currencyEnum) {

        Optional<CurrencyEntity> currencyOpt = currencyRepository.findByCode(currencyEnum);
        if (currencyOpt.isEmpty()) {
            CurrencyEntity currencyEntity = new CurrencyEntity();
            currencyEntity.setCode(currencyEnum);
            currencyRepository.save(currencyEntity);
        }
    }



}
