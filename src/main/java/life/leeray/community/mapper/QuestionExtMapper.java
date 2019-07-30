package life.leeray.community.mapper;

import life.leeray.community.model.Question;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/28 0028 12:32
 */
public interface QuestionExtMapper {
    int incView(Question record);

    int incComment(Question record);
}
