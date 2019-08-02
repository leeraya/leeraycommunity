package life.leeray.community;

import life.leeray.community.mapper.UserMapper;
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


    @Value("${spring.datasource.username}")
    String name;
    @Value("${spring.datasource.password}")
    String password;

    @Test
    public void Test() {
        System.out.println(name + " " + password);
    }

}
