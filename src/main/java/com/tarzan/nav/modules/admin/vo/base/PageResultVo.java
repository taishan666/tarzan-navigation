package com.tarzan.nav.modules.admin.vo.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@NoArgsConstructor
public class  PageResultVo<T> implements Serializable {
    private List<T> rows;
    private Long total;

    public PageResultVo(List<T> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }
}
