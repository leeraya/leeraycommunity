package life.leeray.community.model;

import lombok.Data;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/24 0024 16:27
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;

    public Question(String title, String description, String tag) {
        this.title = title;
        this.description = description;
        this.tag = tag;
    }
}
