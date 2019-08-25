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
import life.leeray.community.utils.PageValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;

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
            page = PageValidator.validator(page, size, count);
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
        page = PageValidator.validator(page, size, count);
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
        Question question;
        if (redisTemplate.hasKey("question" + id)) {
            question = (Question) redisTemplate.opsForValue().get("question" + id);
        } else {
            question = questionMapper.selectByPrimaryKey(id);
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //再将刚才被访问的问题放入redis
            redisTemplate.opsForValue().set("question" + id, question, 600, TimeUnit.SECONDS);
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
            //新发布的问题被访问的几率可能大一点，所以将它房屋redis中
            if (!redisTemplate.hasKey("question" + question.getId())) {
                redisTemplate.opsForValue().set("question" + question.getId(), question, 800, TimeUnit.SECONDS);
            }
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
            if (redisTemplate.hasKey("question" + question.getId())) {
                redisTemplate.opsForValue().set("question" + question.getId()
                        , questionMapper.selectByPrimaryKey(question.getId()), 600, TimeUnit.SECONDS);
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
            if (redisTemplate.hasKey("question" + id)) {
                redisTemplate.delete("question" + id);
            }
        }
    }

    /**
     * 寻找热门问题列表
     *
     * @return
     */
    public List<Question> findHotQuestions() {
        //查找热门问题预选
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCommentCountGreaterThan(0)
                .andViewCountGreaterThan(0);
        List<Question> questions = questionMapper.selectByExampleWithBLOBs(example);
        //按照自定义热度算法排序
        Collections.sort(questions, (q1, q2) -> {
            if (((q1.getViewCount() * 2 + q1.getCommentCount() * 8) / 10.0) > ((q2.getViewCount() * 2 + q2.getCommentCount() * 8) / 10)) {
                return -1;
            } else if (((q1.getViewCount() * 2 + q1.getCommentCount() * 8) / 10.0) < ((q2.getViewCount() * 2 + q2.getCommentCount() * 8) / 2)) {
                return 1;
            } else {
                if (q1.getGmtModified() >= q2.getGmtModified()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        //要返回的列表，空间有限，只返回前面一部分的数据
        ArrayList<Question> ret = new ArrayList<>();
        if (questions.size() > 6) {
            //同时考虑热门问题被访问的概率大，将它们放入redis缓存中，提高访问效率
            for (int i = 0; i < 6; i++) {
                Question e = questions.get(i);
                ret.add(e);
                if (!redisTemplate.hasKey("question" + e.getId())) {
                    redisTemplate.opsForValue().set("question" + e.getId(), e, 800, TimeUnit.SECONDS);
                }
            }
            return ret;
        } else {
            for (Question question : questions) {
                if (redisTemplate.hasKey("question" + question.getId())) {
                    redisTemplate.opsForValue().set("question" + question.getId(), question, 800, TimeUnit.SECONDS);
                }
            }
            return questions;
        }
    }

    /**
     * 在加载首页时，加载热门标签
     * 实现逻辑：先从数据库中查找所有问题的标签，然后按,分割标签，使用Map统计各个标签的次数，次数多的标签就是热门标签
     *
     * @return
     */
    public List<String> findHotTags() {
        List<String> hotTags = questionExtMapper.findHotTags();
        Map<String, Long> tagMap = new TreeMap<>();
        for (String hotTag : hotTags) {
            //一个问题的标签可能有多个，将它们分割
            String[] tag = hotTag.split(",");
            for (String s : tag) {
                if (!tagMap.containsKey(s)) {
                    tagMap.put(s, 1L);
                } else {
                    tagMap.put(s, tagMap.get(s) + 1);
                }
            }
        }
        //list中存的是k-v对
        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(tagMap.entrySet());
        Collections.sort(list, (kv1, kv2) -> {
            return kv2.getValue().compareTo(kv1.getValue());
        });
        List<String> ret = new ArrayList<>();
        if (list.size() > 10) {
            for (int i = 0; i < 10; i++) {
                ret.add(list.get(i).getKey());
            }
        } else {
            for (Map.Entry<String, Long> stringLongEntry : list) {
                ret.add(stringLongEntry.getKey());
            }
        }
        return ret;
    }
}
