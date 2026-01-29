package tech.sangdang.invoicer.modules.account.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;
import tech.sangdang.invoicer.modules.system.SystemConfig;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountQueryPortImpl implements AccountQueryPort {
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
    private final SystemConfig systemConfig;

    @Override
    public boolean existsUserById(String userId) {
        try {
            var result = this.getUserById(userId);
            return result.isPresent();
        } catch(UserNotFoundException e) {
            return false;
        }
    }

    @Override
    public Optional<UserDto> getUserById(String userId) {
        AdminGetUserRequest request = AdminGetUserRequest.builder()
                .username(userId)
                .userPoolId(systemConfig.getUserPoolId())
                .build();

        var response = cognitoIdentityProviderClient.adminGetUser(request);
        log.debug("Found user: {}", response);
        return Optional.of(UserDto.builder()
                .sub(response.userAttributes().stream().filter(i -> i.name().equals("sub")).findFirst().map(AttributeType::value).orElse(null))
                .email(response.userAttributes().stream().filter(i -> i.name().equals("email")).findFirst().map(AttributeType::value).orElse(null))
                .build());
    }

    @Override
    public List<UserDto> listUsers() {
        ListUsersRequest request = ListUsersRequest.builder()
                .userPoolId(systemConfig.getUserPoolId())
                .attributesToGet("sub", "email")
                .build();

        try {
            var response = cognitoIdentityProviderClient.listUsers(request);
            log.debug("Found {} users", response.users().size());
            return response.users().stream().map(user -> UserDto.builder()
                            .sub(user.attributes().stream().filter(i -> i.name().equals("sub")).findFirst().map(AttributeType::value).orElse(null))
                            .email(user.attributes().stream().filter(i -> i.name().equals("email")).findFirst().map(AttributeType::value).orElse(null))
                            .build())
                    .toList();
        } catch (Exception e) {
            log.error("Error listing users", e);
            throw e;
        }
    }

    @Override
    public List<UserDto> listUsers(String role) {
        ListUsersInGroupRequest request = ListUsersInGroupRequest.builder()
                .userPoolId(systemConfig.getUserPoolId())
                .groupName(role)
                .build();

        try {
            var response = cognitoIdentityProviderClient.listUsersInGroup(request);
            log.debug("Found {} users", response.users().size());
            return response.users().stream().map(user -> UserDto.builder()
                            .sub(user.attributes().stream().filter(i -> i.name().equals("sub")).findFirst().map(AttributeType::value).orElse(null))
                            .email(user.attributes().stream().filter(i -> i.name().equals("email")).findFirst().map(AttributeType::value).orElse(null))
                            .build())
                    .toList();
        } catch (Exception e) {
            log.error("Error listing users", e);
            throw e;
        }
    }
}
