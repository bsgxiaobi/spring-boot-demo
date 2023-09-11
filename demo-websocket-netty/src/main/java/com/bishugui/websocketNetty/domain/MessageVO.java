package com.bishugui.websocketNetty.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author bi shugui
 * @description 消息实体
 * @date 2023/9/11 21:45
 */
@Data
public class MessageVO {
    private Object data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String type;
}
