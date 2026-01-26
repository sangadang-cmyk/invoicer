package tech.sangdang.invoicer.modules.account.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.account.api.AccountTestController;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;
import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AccountTestController.PATH)
public class AccountTestControllerImpl implements AccountTestController {
    private final AccountQueryPort accountQueryPort;

    @Override
    public List<UserDto> listUsers() {
        return accountQueryPort.listUsers();
    }

    @Override
    public boolean existsAccountId(String accountId) {
        return accountQueryPort.existsUserById(accountId);
    }
}
