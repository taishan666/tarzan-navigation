package com.tarzan.nav.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author tarzan liu
 * @date 2020/5/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthenticator extends Authenticator {

    private String username;

    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}

