package liu.hope.idempotent_demo.autoconfigure;


import liu.hope.idempotent_demo.lock.DistributedLock;
import liu.hope.idempotent_demo.lock.DistributedLockMysql;
import liu.hope.idempotent_demo.lock.DistributedLockRedis;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 分布式锁自动配置
 */
@Configuration
public class DistributedLockAutoConfiguration {

    @Autowired(required = false)
    private DataSource dataSource;

    @Bean("distributedLockMysql")
    public DistributedLock distributedLockMysql() {
        return new DistributedLockMysql(dataSource);
    }

    @Bean("distributedLockRedis")
    @Primary
    @DependsOn("distributedLockMysql")
    public DistributedLock distributedLockRedis(RedissonClient redissonClient, DistributedLockMysql distributedLockMysql) {
        return new DistributedLockRedis(redissonClient, distributedLockMysql);
    }


}