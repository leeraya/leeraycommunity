package life.leeray.community.service;

import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

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
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        User dbUser = null;
        if (users.size() != 0){
            dbUser = users.get(0);
        }
        if (dbUser == null) {
            //插入新纪录
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
