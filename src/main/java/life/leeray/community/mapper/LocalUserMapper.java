package life.leeray.community.mapper;

import java.util.List;
import life.leeray.community.model.LocalUser;
import life.leeray.community.model.LocalUserExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface LocalUserMapper {
    int countByExample(LocalUserExample example);

    int deleteByExample(LocalUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LocalUser record);

    int insertSelective(LocalUser record);

    List<LocalUser> selectByExampleWithRowbounds(LocalUserExample example, RowBounds rowBounds);

    List<LocalUser> selectByExample(LocalUserExample example);

    LocalUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LocalUser record, @Param("example") LocalUserExample example);

    int updateByExample(@Param("record") LocalUser record, @Param("example") LocalUserExample example);

    int updateByPrimaryKeySelective(LocalUser record);

    int updateByPrimaryKey(LocalUser record);
}