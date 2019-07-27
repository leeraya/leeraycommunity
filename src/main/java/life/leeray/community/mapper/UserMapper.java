package life.leeray.community.mapper;

import life.leeray.community.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/7/23 0023 16:05
 */
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(name,account_id,token,gmt_create,gmt_modified,avatar_url) " +
            "VALUES(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);

    @Select("SELECT * FROM user WHERE account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("UPDATE user SET name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl}" +
            "WHERE id = #{id}")
    void update(User user);
}
