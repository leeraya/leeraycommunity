package life.leeray.community.controller;

import life.leeray.community.dto.QuestionDTO;
import life.leeray.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/26 0026 17:16
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model) {
        questionService.incView(id);
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("question", question);
        return "question";
    }
}
