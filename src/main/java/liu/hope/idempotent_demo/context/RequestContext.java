package liu.hope.idempotent_demo.context;

import java.util.Map;

public interface RequestContext {

    RequestContext add(String key, String value);

    String get(String key);

    RequestContext remove(String key);

    Map<String, String> getAttributes();

}
