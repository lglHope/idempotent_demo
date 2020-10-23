package liu.hope.idempotent_demo.storage;

import liu.hope.idempotent_demo.enums.IdempotentStorageTypeEnum;

import java.util.concurrent.TimeUnit;

/**
 * 幂等存储接口
 *
 */
public interface IdempotentStorage {

    String COLL_NAME = "idempotent_record";

    IdempotentStorageTypeEnum type();

    void setValue(String key, String value, long expireTime, TimeUnit timeUnit);

    String getValue(String key);

}
