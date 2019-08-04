package life.leeray.community.controller;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/22 0022 15:22
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search) {

        PaginationDTO pagination = questionService.list(search, page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
