package life.leeray.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/31 0031 16:30
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
