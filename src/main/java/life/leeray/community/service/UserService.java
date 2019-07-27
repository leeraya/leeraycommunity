package life.leeray.community.service;

import life.leeray.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/27 0027 8:46
 */
@Controller
public class UserService {
    @Autowired
    private UserMapper userMapper;
}
