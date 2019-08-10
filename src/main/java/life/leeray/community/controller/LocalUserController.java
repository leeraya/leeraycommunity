package life.leeray.community.controller;

import life.leeray.community.dto.ResultDTO;
import life.leeray.community.mapper.LocalUserMapper;
import life.leeray.community.model.LocalUser;
import life.leeray.community.model.LocalUserExample;
import life.leeray.community.utils.CodeVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/9 0009 16:25
 */
@Controller
public class LocalUserController {

    @Autowired
    private LocalUserMapper localUserMapper;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/regist")
    public String regist() {
        return "regist";
    }


    @RequestMapping(value = "/doRegist", method = RequestMethod.POST)
    @ResponseBody
    public Object doRegist(HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");
        String email = request.getParameter("email");
        String captcha = request.getParameter("captcha");

        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("confirm_password", confirm_password);
        model.addAttribute("email", email);

        //获取session中生成的验证码
        String imageCode = (String) request.getSession().getAttribute("imageCode");


        if (!imageCode.toUpperCase().equals(captcha.toUpperCase())) {
            return ResultDTO.captchaError();
        }

        LocalUserExample example1 = new LocalUserExample();
        example1.createCriteria().andNameEqualTo(username);
        localUserMapper.selectByExample(example1);

        return ResultDTO.okOff();
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

}
