package life.leeray.community.dto;

import life.leeray.community.model.User;
import lombok.Data;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/1 0001 16:42
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private Long outerid;
    private String outerTitle;
    private Integer type;
    private String typeName;
}
