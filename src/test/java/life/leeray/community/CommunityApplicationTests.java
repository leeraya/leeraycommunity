package life.leeray.community;

import life.leeray.community.mapper.UserMapper;
import life.leeray.community.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    public void contextLoads() {
        User user = userMapper.findById(1);
        System.out.println(user.toString());
    }

    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String password;
    @Test
    public void Test(){
        System.out.println(name+" "+password);
    }
}
