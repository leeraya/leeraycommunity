package life.leeray.community.controller;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import life.leeray.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 9:00
 */
@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @GetMapping(value = "/profile/{action}",produces = "text/plain;charset=UTF-8")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", pagination);

        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}
