package life.leeray.community.controller;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.model.Question;
import life.leeray.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/22 0022 15:22
 * 首页控制器
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    /**
     * @param model
     * @param page   要跳转到的页数
     * @param size   每夜数据条数
     * @param search 搜索关键字
     * @return
     */
    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        //添加热门问题列表
        List<Question> hotQuestions = questionService.findHotQuestions();
        //热门标签
        List<String> hotTags = questionService.findHotTags();

        PaginationDTO pagination = questionService.list(search, tag, sort, page, size);//根据关键字，页数，条数搜索问题
        model.addAttribute("hotQuestions", hotQuestions);
        model.addAttribute("hotTags", hotTags);

        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);//将search关键字放回model中，前端可能还需要。
        model.addAttribute("tag", tag);
        model.addAttribute("sort", sort);
        return "index";
    }
}
