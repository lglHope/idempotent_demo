package liu.hope.idempotent_demo.autoconfigure;

import liu.hope.idempotent_demo.aspect.DistributedIdempotentAspect;
import liu.hope.idempotent_demo.idempotent.DistributedIdempotent;
import liu.hope.idempotent_demo.idempotent.DistributedIdempotentImpl;
import liu.hope.idempotent_demo.properties.IdempotentProperties;
import liu.hope.idempotent_demo.storage.IdempotentStorageFactory;
import liu.hope.idempotent_demo.storage.IdempotentStorageMongo;
import liu.hope.idempotent_demo.storage.IdempotentStorageMysql;
import liu.hope.idempotent_demo.storage.IdempotentStorageRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2020-04-03 23:27
 */
@Configuration
@ImportAutoConfiguration(IdempotentProperties.class)
@AutoConfigureAfter(liu.hope.idempotent_demo.autoconfigure.DistributedLockAutoConfiguration.class)
public class IdempotentAutoConfiguration {

    @Autowired
    private DistributedIdempotent distributedIdempotent;

    @Bean
    public DistributedIdempotent distributedIdempotent() {
        return new DistributedIdempotentImpl();
    }

    @Bean
    public DistributedIdempotentAspect distributedIdempotentAspect() {
        return new DistributedIdempotentAspect(distributedIdempotent);
    }

    @Bean
    public IdempotentStorageFactory idempotentStorageFactory() {
        return new IdempotentStorageFactory();
    }

    @ConditionalOnClass(MongoTemplate.class)
    @Configuration
    protected static class MongoTemplateConfiguration {
        @Bean
        public IdempotentStorageMongo idempotentStorageMongo() {
            return new IdempotentStorageMongo();
        }
    }


    @ConditionalOnClass(JdbcTemplate.class)
    @Configuration
    protected static class JdbcTemplateConfiguration {
        @Bean
        public IdempotentStorageMysql idempotentStorageMysql() {
            return new IdempotentStorageMysql();
        }
    }

    @Bean
    public IdempotentStorageRedis idempotentStorageRedis() {
        return new IdempotentStorageRedis();
    }
}