package life.leeray.community.controller;

import life.leeray.community.dto.CommentDTO;
import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.dto.QuestionDTO;
import life.leeray.community.enums.ContentTypeEnum;
import life.leeray.community.model.User;
import life.leeray.community.service.CommentService;
import life.leeray.community.service.QuestionService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 17:16
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model,
                           HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("游客");
            user.setAvatarUrl("/images/default-avatar.png");
        }

        questionService.incView(id);//增加阅读数
        QuestionDTO question = questionService.getById(id);
        List<CommentDTO> comments = commentService.listByTargetId(id, ContentTypeEnum.QUESTION);
        List<QuestionDTO> relatedQuestions = commentService.selectRelated(question);

        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("user", user);

        return "question";
    }

    @RequestMapping("/question/delete")
    public String delete(@RequestParam(value = "id") Long id,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                         HttpServletRequest request,
                         Model model) {
        User user = (User) request.getSession().getAttribute("user");
        //非法进入，返回首页
        if (user == null) {
            return "redirect:/";
        }
        questionService.delete(id, user);
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("section", "questions");
        model.addAttribute("sectionName", "我的问题");
        return "profile";
    }
}
