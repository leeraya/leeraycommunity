package life.leeray.community.mapper;

import life.leeray.community.dto.QuestionQueryDTO;
import life.leeray.community.model.Question;

import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 12:32
 */
public interface QuestionExtMapper {
    int incView(Question record);

    int incComment(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
