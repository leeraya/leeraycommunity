package life.leeray.community.controller;

import life.leeray.community.dto.ResultDTO;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.model.UserExample;
import life.leeray.community.service.UserService;
import life.leeray.community.utils.CodeVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/9 0009 16:25
 */
@Controller
public class LocalUserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/regist")
    public String regist() {
        return "regist";
    }


    //用户注册
    @RequestMapping(value = "/doRegist", method = RequestMethod.POST)
    @ResponseBody
    public Object doRegist(HttpServletRequest request,
                           HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");
        String email = request.getParameter("email");
        String captcha = request.getParameter("captcha");

        //获取session中生成的验证码
        String imageCode = (String) request.getSession().getAttribute("imageCode");
        if (!imageCode.toUpperCase().equals(captcha.toUpperCase())) {
            return ResultDTO.captchaError();
        } else if (!password.equals(confirm_password)) {
            return ResultDTO.confirmError();
        } else if (password.length() < 6 || password.length() > 30) {
            return ResultDTO.pwdLengthError();
        }
        if (userService.usernameExists(username)) {
            return ResultDTO.duplicateName();
        }
        if (userService.emailExists(email)) {
            return ResultDTO.duplicateEmail();
        }
        //如果都经过了检查，那么就将用户写入数据库
        User lcUser = new User();
        lcUser.setAvatarUrl("/images/default-avatar.png");//默认头像,后面可以改的
        lcUser.setGmtCreate(System.currentTimeMillis());
        lcUser.setGmtModified(lcUser.getGmtCreate());
        lcUser.setName(username);
        lcUser.setPassword(password);
        lcUser.setEmail(email);
        String token = UUID.randomUUID().toString();
        response.addCookie(new Cookie("token", token));
        lcUser.setToken(token);
        userMapper.insertSelective(lcUser);
        return ResultDTO.okOff();
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object doLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(username)
                .andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            return ResultDTO.UnknowUser();
        } else {
            User user = users.get(0);
            response.addCookie(new Cookie("token", user.getToken()));
            return ResultDTO.okOff();
        }
    }

    //自动生成验证码方法
    @RequestMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = CodeVerify.createImage();
        //将验证码存入Session
        session.setAttribute("imageCode", objs[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    /**
     * 更新用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/updateInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO updateUserInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");

        String infoName = request.getParameter("info-name").trim();
        String infoEmail = request.getParameter("info-email").trim();

        if (!infoName.equals(user.getName()) && userService.usernameExists(infoName)) {
            return ResultDTO.duplicateName();
        }
        if (!infoEmail.equals(user.getEmail()) && userService.emailExists(infoEmail)) {
            return ResultDTO.duplicateEmail();
        }
        user.setName(infoName);
        user.setEmail(infoEmail);
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        if (redisTemplate.hasKey("user" + user.getToken())) {
            redisTemplate.opsForValue().set("user" + user.getToken(), user, 600, TimeUnit.SECONDS);
        }
        return ResultDTO.okOff();
    }

    /**
     * 用户登出
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();//从request中获取session
        User user = (User) session.getAttribute("user");
        String token = user.getToken();
        if (redisTemplate.hasKey("user" + token)) {
            redisTemplate.delete("user" + token);
        }
        session.removeAttribute("user");
        session.removeAttribute("unreadCount");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }

    /**
     * 找回密码，根据传进来的用户名找到用户的邮箱，向用户发送修改密码的邮件
     * 考虑：邮箱传送什么内容？
     *
     * @return
     */
    @RequestMapping(value = "/user/findPwd")
    @ResponseBody
    public ResultDTO findPassword(HttpServletRequest request) {
        return null;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/user/updatePwd")
    @ResponseBody
    public ResultDTO updatePassword(HttpServletRequest request) {
        return null;
    }
}
