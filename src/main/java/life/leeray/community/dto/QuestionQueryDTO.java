package life.leeray.community.dto;

import lombok.Data;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/4 0004 10:31
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}