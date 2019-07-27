package life.leeray.community.mapper;

import life.leeray.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/24 0024 16:21
 */
@Mapper
public interface QuestionMapper {
    //插入一条话题
    @Insert("INSERT INTO question(title,description,tag,gmt_create,gmt_modified,creator)" +
            "VALUES(#{title},#{description},#{tag},#{gmtCreate},#{gmtModified},#{creator})")
    void create(Question question);

    @Select("SELECT * FROM question ORDER BY gmt_create DESC LIMIT #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM question")
    Integer count();

    @Select("SELECT * FROM question WHERE creator = #{id} ORDER BY gmt_create DESC LIMIT #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "id") Integer id, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM question WHERE creator = #{id}")
    Integer countByUserId(Integer id);

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question getById(@Param("id") Integer id);
}
