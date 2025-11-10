package com.hubEleven.user.domain.vo;

import com.hubEleven.common.exception.GlobalException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.hubEleven.user.domain.exception.ErrorCode.INVALID_PASSWORD;
import static com.hubEleven.user.domain.exception.ErrorCode.INVALID_USERNAME;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {
    private String username;
    private String password;
    private String name;
    private String slackId;
    private String phoneNumber;

    public UserInfo(
            String username,
            String password,
            String name,
            String slackId,
            String phoneNumber
    ) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        this.password = password;
        this.name = name;
        this.slackId = slackId;
        this.phoneNumber = phoneNumber;
    }
    public void validateUsername(String username) {
        if (username.length() < 4 || username.length() > 10) {
            throw new GlobalException(INVALID_USERNAME);
        }

        String pattern = "^[a-z0-9]{4,10}$";
        if (!username.matches(pattern)) {
            throw new GlobalException(INVALID_USERNAME);
        }
    }
    public void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 15) {
            throw new GlobalException(INVALID_PASSWORD);
        }

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,15}$";
        if (password.matches(pattern)) {
            throw new GlobalException(INVALID_PASSWORD);
        }
    }
}
