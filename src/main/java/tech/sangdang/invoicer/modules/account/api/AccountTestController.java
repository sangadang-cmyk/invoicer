package tech.sangdang.invoicer.modules.account.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;

@Tag(name = "_Test")
public interface AccountTestController {
    String PATH = "/public/test/account";

    @GetMapping("/batch")
    default List<UserDto> listUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @GetMapping("/{accountId}/exists")
    default boolean existsAccountId(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
