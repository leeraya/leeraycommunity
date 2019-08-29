package life.leeray.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/4 0004 10:31
 */
@Data
public class QuestionQueryDTO implements Serializable {
    private String search;
    private String sort;
    private Long time;
    private String tag;
    private Integer page;
    private Integer size;
}