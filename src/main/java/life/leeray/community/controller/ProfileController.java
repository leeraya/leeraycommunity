package life.leeray.community.controller;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.model.User;
import life.leeray.community.service.NotificationService;
import life.leeray.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 9:00
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;


    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "10") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", pagination);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
        } else if ("replies".equals(action)) {
            PaginationDTO pagination = notificationService.list(user.getId(), page, size);
            int unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", pagination);
            model.addAttribute("unreadCount", unreadCount);
        }
        return "profile";
    }
}
