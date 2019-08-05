package life.leeray.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/22 0022 16:19
 */
@Data
public class GithubUser implements Serializable {
    private Long id;
    private String name;//名称
    private String bio;//简介
    private String avatarUrl;//头像url
}
