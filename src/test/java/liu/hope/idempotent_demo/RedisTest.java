package liu.hope.idempotent_demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void getKeys() {
        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

}
