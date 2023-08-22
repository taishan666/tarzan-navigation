package com.tarzan.nav.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tarzan
 */
@Getter
@AllArgsConstructor
public enum NavigationTypeEnum {

    SITE( 1,"site"),


    POST( 2,"post"),;

    final int type;
    final String name;

}
