package liu.hope.idempotent_demo.aspect;

import liu.hope.idempotent_demo.idempotent.DistributedIdempotent;
import liu.hope.idempotent_demo.context.ContextHolder;
import liu.hope.idempotent_demo.exception.IdempotentException;
import liu.hope.idempotent_demo.request.IdempotentRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
public class DistributedIdempotentAspect extends AbstractIdempotentAspectSupport {

    private DistributedIdempotent distributedIdempotent;

    public DistributedIdempotentAspect(DistributedIdempotent distributedIdempotent) {
        this.distributedIdempotent = distributedIdempotent;
    }

    @Around(value = "@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinpoint, Idempotent idempotent) throws Throwable {
        // 获取当前访问的方法
        Method method = ((MethodSignature) joinpoint.getSignature()).getMethod();
        // 获取当前方法的参数
        Object[] args = joinpoint.getArgs();

        String key = "";
        // 判断当前的注解中是否有spel表达式,以此来获取幂等key
        if (StringUtils.hasText(idempotent.spelKey())) {
            key = parseKey(idempotent.spelKey(), method, args);
        } else {
            // 如果没有,就自己使用一个方法获得一个幂等key
            key = ContextHolder.getCurrentContext().get("globalIdempotentId");
        }

        String userInputKey = idempotent.value();
        if (!StringUtils.hasText(userInputKey)) {
            userInputKey = method.getName();
        }
        String idempotentKey = userInputKey + ":" + key;

        IdempotentRequest request = IdempotentRequest.builder().key(idempotentKey)
                .firstLevelExpireTime(idempotent.firstLevelExpireTime())
                .secondLevelExpireTime(idempotent.secondLevelExpireTime())
                .timeUnit(idempotent.timeUnit())
                .lockExpireTime(idempotent.lockExpireTime())
                .readWriteType(idempotent.readWriteType())
                .build();

        try {
            return distributedIdempotent.execute(request, () -> {
                try {
                    return joinpoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }, () -> {
                throw new IdempotentException("重复请求");
            });
        } catch (IdempotentException ex) {
            // 异常时,调用父类的异常处理
            return handleIdempotentException(joinpoint, idempotent, ex);
        }
    }

    /**
     * 获取幂等的key, 支持SPEL表达式
     * @param key
     * @param method
     * @param args
     * @return
     */
    private String parseKey(String key, Method method, Object[] args){
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = nameDiscoverer.getParameterNames(method);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for(int i = 0;i < paraNameArr.length; i++){
            context.setVariable(paraNameArr[i], args[i]);
        }
        try {
            return parser.parseExpression(key).getValue(context, String.class);
        } catch (SpelEvaluationException e) {
            throw new RuntimeException("SPEL表达式解析错误", e);
        }
    }

}
