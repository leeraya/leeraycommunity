package life.leeray.community.service;

import life.leeray.community.dto.EmailDTO;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

    @Value("${customize-img-prefix}")
    private String imgPrefix;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        User dbUser = null;
        if (users.size() != 0) {
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
            //github用户头像的更新规则：默认使用github头像，如果用户在资料中心更换头像，
            // 则用户头像不再随github的头像更换而改变
            //如果头像是在本站内更换的，那么不会更新github头像的,反之，如果没有在本站更换过，那么就随github的变
            if (dbUser.getAvatarUrl().indexOf(imgPrefix) == -1) {
                updateUser.setAvatarUrl(user.getAvatarUrl());
            }
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    /**
     * 验证用户名是否存在
     */
    public boolean usernameExists(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证邮箱是否存在
     */
    public boolean emailExists(String email) {
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(example);
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发送邮件
     *
     * @param emailDTO
     */
    public void sendEmail(EmailDTO emailDTO) {

    }
}
