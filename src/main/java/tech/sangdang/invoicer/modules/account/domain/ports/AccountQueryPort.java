package tech.sangdang.invoicer.modules.account.domain.ports;

import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;
import java.util.Optional;

public interface AccountQueryPort {
    boolean existsUserById(String userId);
    Optional<UserDto> getUserById(String userId);
    List<UserDto> listUsers();
    List<UserDto> listUsers(String role);
}
