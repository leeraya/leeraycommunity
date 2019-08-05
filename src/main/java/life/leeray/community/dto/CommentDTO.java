package life.leeray.community.dto;

import life.leeray.community.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/30 0030 10:47
 */
@Data
public class CommentDTO implements Serializable {
    private Long id;

    private Long parentId;

    private Integer type;

    private Long commentator;

    private Long gmtCreate;

    private Long gmtModified;

    private Long likeCount;

    private String content;

    private Integer commentCount;

    private User user;
}
