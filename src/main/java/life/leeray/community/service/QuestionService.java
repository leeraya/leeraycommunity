package life.leeray.community.service;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.dto.QuestionDTO;
import life.leeray.community.mapper.QuestionMapper;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.Question;
import life.leeray.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/25 0025 10:37
 */
@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public static Integer validator(Integer page, Integer size, Integer count) {
        //边界验证，容错处理
        Integer totalPage = count % size == 0 ? count / size : count / size + 1;
        page = page < 1 ? 1 : page;
        page = page > totalPage ? totalPage : page;
        return page;
    }

    public PaginationDTO list(Integer page, Integer size) {
        Integer count = questionMapper.count();
        page = validator(page, size, count);
        //计算用于limit m,n所需的m值
        Integer offset = size * (page - 1);
        List<Question> list = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList();

        if (list != null || list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Question question = list.get(i);
                QuestionDTO questionDTO = new QuestionDTO();
                User user = userMapper.findById(question.getCreator());
                questionDTO.setQuestion(question);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestion(questionDTOList);
        paginationDTO.setPagination(count, page, size);
        return paginationDTO;
    }

    public PaginationDTO list(Integer id, Integer page, Integer size) {
        Integer count = questionMapper.countByUserId(id);
        page = validator(page, size, count);
        //计算用于limit m,n所需的m值
        Integer offset = size * (page - 1);
        List<Question> list = questionMapper.listByUserId(id,offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList();

        if (list != null || list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Question question = list.get(i);
                QuestionDTO questionDTO = new QuestionDTO();
                User user = userMapper.findById(question.getCreator());
                questionDTO.setQuestion(question);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestion(questionDTOList);
        paginationDTO.setPagination(count, page, size);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion(question);
        questionDTO.setUser(user);
        return questionDTO;
    }
}
