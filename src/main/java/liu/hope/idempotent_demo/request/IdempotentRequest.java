package liu.hope.idempotent_demo.request;

import liu.hope.idempotent_demo.enums.ReadWriteTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 幂等锁的相关信息
 */
@Data
@Builder
public class IdempotentRequest {

    /**
     * 幂等Key
     */
    private String key;

    /**
     * 一级存储过期时间
     */
    private int firstLevelExpireTime;

    /**
     * 二级存储过期时间
     */
    private int secondLevelExpireTime;

    /**
     * 锁的过期时间
     */
    private int lockExpireTime;

    /**
     * 存储时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 读写类型
     */
    private ReadWriteTypeEnum readWriteType;

}