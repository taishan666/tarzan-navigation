package com.tarzan.navigation.modules.admin.vo.base;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVo implements Serializable {
    private List<?> rows;
    private Long total;

}
