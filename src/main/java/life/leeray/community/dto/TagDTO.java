package life.leeray.community.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/31 0031 16:30
 */
@Data
public class TagDTO implements Serializable {
    private String categoryName;
    private List<String> tags;
}
