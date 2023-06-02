package com.tarzan.nav.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author tarzan liu
 * @date 2020/5/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleEmail {

    private String subject;

    private String content;
}
