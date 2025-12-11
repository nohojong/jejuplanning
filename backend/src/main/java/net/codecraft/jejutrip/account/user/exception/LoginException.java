package net.codecraft.jejutrip.account.user.exception;

import net.codecraft.jejutrip.common.response.message.AccountMessage;

public class LoginException extends RuntimeException{

    public LoginException(AccountMessage message) {
        super(message.getMessage());
    }

    public LoginException(String message) {
        super(message);
    }
}
