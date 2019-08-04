package life.leeray.community.service;

import life.leeray.community.dto.PaginationDTO;
import life.leeray.community.dto.QuestionDTO;
import life.leeray.community.dto.QuestionQueryDTO;
import life.leeray.community.exception.CustomizeErrorCode;
import life.leeray.community.exception.CustomizeException;
import life.leeray.community.mapper.QuestionExtMapper;
import life.leeray.community.mapper.QuestionMapper;
import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.Question;
import life.leeray.community.model.QuestionExample;
import life.leeray.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public static Integer validator(Integer page, Integer size, Integer count) {
        //边界验证，容错处理
        Integer totalPage = count % size == 0 ? count / size : count / size + 1;
        page = page < 1 ? 1 : page;
        page = page > totalPage ? totalPage : page;
        return page;
    }

    public PaginationDTO list(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        //查询参数的DTO
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        //查询符合条件的问题的条数
        Integer count = questionExtMapper.countBySearch(questionQueryDTO);

        List<Question> questions = new ArrayList<>();
        //有符合条件的问题
        if (count != 0) {
            page = validator(page, size, count);
            //计算用于limit m,n所需的m值
            Integer offset = size * (page - 1);
            questionQueryDTO.setPage(offset);
            questionQueryDTO.setSize(size);
            questions = questionExtMapper.selectBySearch(questionQueryDTO);
        }
        List<QuestionDTO> questionDTOList = new ArrayList();
        if (questions != null || questions.size() != 0) {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                QuestionDTO questionDTO = new QuestionDTO();
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                questionDTO.setQuestion(question);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(count, page, size);
        return paginationDTO;
    }

    /**
     * @param id
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long id, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        Integer count = questionMapper.countByExample(questionExample);
        page = validator(page, size, count);
        //计算用于limit m,n所需的m值
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        example.setOrderByClause("gmt_modified DESC");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList();

        if (questions != null || questions.size() != 0) {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                QuestionDTO questionDTO = new QuestionDTO();
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                questionDTO.setQuestion(question);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(count, page, size);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion(question);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (null == question.getId()) {
            //如果没有id，那么说明是新发布的，直接存入数据库。
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            //是更新的发布
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    /**
     * 增加浏览数
     *
     * @param id 被点击的问题的id
     */
    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
    }

    /**
     * 删除问题
     *
     * @param id   问题id
     * @param user 当前seesion中的用户信息
     */
    public void delete(Long id, User user) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question != null && user.getId().equals(question.getCreator())) {
            questionMapper.deleteByPrimaryKey(id);
        }
    }
}
