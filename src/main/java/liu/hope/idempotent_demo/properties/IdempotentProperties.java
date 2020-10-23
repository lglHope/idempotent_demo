package liu.hope.idempotent_demo.properties;

import liu.hope.idempotent_demo.enums.IdempotentStorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kitty.idempontent")
public class IdempotentProperties {

    /**
     * 一级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String firstLevelType = IdempotentStorageTypeEnum.REDIS.name();

    /**
     * 二级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String secondLevelType;

}
