package life.leeray.community.controller;

import life.leeray.community.dto.CommentCreateDTO;
import life.leeray.community.dto.CommentDTO;
import life.leeray.community.dto.ResultDTO;
import life.leeray.community.enums.ContentTypeEnum;
import life.leeray.community.exception.CustomizeErrorCode;
import life.leeray.community.mapper.CommentMapper;
import life.leeray.community.model.Comment;
import life.leeray.community.model.User;
import life.leeray.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 14:37
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);

        commentService.insert(comment,user);
        return ResultDTO.okOff();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable("id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, ContentTypeEnum.CONMENT);
        return ResultDTO.okOff(commentDTOS);
    }

}
