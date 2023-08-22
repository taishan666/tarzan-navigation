package com.tarzan.nav.modules.front.dto;

import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class RegisterDTO {
    private String user_login;
    private String email_phone;
    private String verification_code;
    private String user_pass;
    private String user_pass2;
    private String user_agreement;
    private String action;
    private String redirect_to;
}
