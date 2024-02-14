package com.tarzan.nav.modules.aichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.nav.modules.aichat.model.ChatItem;
import org.apache.ibatis.annotations.Select;

/**
 * @author tarzan
 */
public interface ChatItemMapper extends BaseMapper<ChatItem> {

    /**
     * 方法描述: 查询当天数据总数
     *
     * @param code
     * @param userId
     * @return {@link int}
     * @throws
     * @author tarzan
     * @date 2024年02月14日 22:36:02
     */
    @Select("SELECT COUNT(*) FROM CHAT_ITEM WHERE DATE(QUESTION_TIME) = CURDATE() AND ANSWER_TYPE=#{code} AND USER_ID=#{userId}")
    int getTodayUsedCnt(Integer code, Integer userId);
}
