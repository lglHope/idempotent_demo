package liu.hope.idempotent_demo.idempotent;

import liu.hope.idempotent_demo.enums.ReadWriteTypeEnum;
import liu.hope.idempotent_demo.request.IdempotentRequest;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface DistributedIdempotent {

    /**
     * 幂等执行
     * @param key 幂等Key
     * @param lockExpireTime 锁的过期时间
     * @param firstLevelExpireTime 一级存储过期时间
     * @param secondLevelExpireTime 二级存储过期时间
     * @param timeUnit 存储时间单位
     * @param readWriteType 读写类型
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    <T> T execute(String key, int lockExpireTime, int firstLevelExpireTime, int secondLevelExpireTime, TimeUnit timeUnit, ReadWriteTypeEnum readWriteType, Supplier<T> execute, Supplier<T> fail);

    /**
     * 幂等执行
     * @param key 幂等Key
     * @param lockExpireTime 锁的过期时间
     * @param firstLevelExpireTime 一级存储过期时间
     * @param secondLevelExpireTime 二级存储过期时间
     * @param timeUnit 存储时间单位
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    <T> T execute(String key, int lockExpireTime, int firstLevelExpireTime, int secondLevelExpireTime, TimeUnit timeUnit, Supplier<T> execute, Supplier<T> fail);

    /**
     * 幂等执行
     * @param request 幂等参数
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    <T> T execute(IdempotentRequest request, Supplier<T> execute, Supplier<T> fail);

    /**
     * 幂等执行
     * @param key 幂等Key
     * @param lockExpireTime 锁的过期时间
     * @param firstLevelExpireTime 一级存储过期时间
     * @param secondLevelExpireTime 二级存储过期时间
     * @param timeUnit 存储时间单位
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    void execute(String key, int lockExpireTime, int firstLevelExpireTime, int secondLevelExpireTime, TimeUnit timeUnit, ReadWriteTypeEnum readWriteType, Runnable execute, Runnable fail);

    /**
     * 幂等执行
     * @param key 幂等Key
     * @param lockExpireTime 锁的过期时间
     * @param firstLevelExpireTime 一级存储过期时间
     * @param secondLevelExpireTime 二级存储过期时间
     * @param timeUnit 存储时间单位
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    void execute(String key, int lockExpireTime, int firstLevelExpireTime, int secondLevelExpireTime, TimeUnit timeUnit, Runnable execute, Runnable fail);

    /**
     * 幂等执行
     * @param request 幂等参数
     * @param execute 要执行的逻辑
     * @param fail Key已经存在，幂等拦截后的执行逻辑
     * @return
     */
    void execute(IdempotentRequest request, Runnable execute, Runnable fail);

}