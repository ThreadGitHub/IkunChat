package org.chat.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author
 * @date 2023/9/15 12:37
 */
@Builder
@Data
public class ChatMessage {
    private Long id;

    private String nickName;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
