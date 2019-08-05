package life.leeray.community.intercepter;

import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.model.UserExample;
import life.leeray.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 16:13
 */
@Component
public class SessionIntercepter implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    List<User> users = new ArrayList<>();
                    User user;
                    if (redisTemplate.hasKey("user" + token)) {
                        user = (User) redisTemplate.opsForValue().get("user" + token);
                        users.add(user);
                    } else {
                        UserExample userExample = new UserExample();
                        userExample.createCriteria().andTokenEqualTo(token);
                        users = userMapper.selectByExample(userExample);
                        if (users.size() != 0) {
                            redisTemplate.opsForValue().set("user" + token, users.get(0), 600, TimeUnit.SECONDS);
                        }
                    }
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        int unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
