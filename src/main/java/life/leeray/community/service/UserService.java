package life.leeray.community.service;

import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
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

    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertUser(user);
        } else {
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            userMapper.update(dbUser);
        }
    }
}
