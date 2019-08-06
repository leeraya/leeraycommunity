package life.leeray.community.controller;

import life.leeray.community.dto.NotificationDTO;
import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.enums.NotificationTypeEnum;
import life.leeray.community.model.User;
import life.leeray.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/2 0002 10:20
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    /**
     * @param id      通知id
     * @param request
     * @return
     */
    @GetMapping(value = "/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/notification/delete")
    public String deleteNotification(@RequestParam(name = "id") Long id,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     HttpServletRequest request,
                                     Model model) {
        User user = (User) request.getSession().getAttribute("user");
        //通过非法方式进入直接返回首页
        if (user == null) {
            return "redirect:/";
        }
        notificationService.delete(id, user);
        PaginationDTO pagination = notificationService.list(user.getId(), page, size);
        model.addAttribute("section", "replies");
        model.addAttribute("sectionName", "最新回复");
        model.addAttribute("pagination", pagination);
        return "profile";
    }
}
