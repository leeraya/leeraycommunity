package life.leeray.community.dto;

import lombok.Data;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 14:48
 */
@Data
public class CommentDTO {
    private long parentId;
    private String content;
    private Integer type;
}
