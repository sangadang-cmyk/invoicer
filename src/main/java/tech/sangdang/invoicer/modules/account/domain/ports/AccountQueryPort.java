package tech.sangdang.invoicer.modules.account.domain.ports;

import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;

public interface AccountQueryPort {
    boolean existsUserById(String userId);
    List<UserDto> listUsers();
}
