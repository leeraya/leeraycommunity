package life.leeray.community.dto;

import lombok.Data;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/17 0017 21:46
 */
@Data
public class EmailDTO {
    private String subject;
    private String text;
    private String to;
    private String from;
}
