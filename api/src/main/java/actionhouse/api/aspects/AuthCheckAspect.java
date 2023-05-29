package actionhouse.api.aspects;

import actionhouse.api.dao.CustomerRepository;
import actionhouse.api.exceptions.AuthorizationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthCheckAspect {
    @Autowired
    private CustomerRepository customerRepository;

    @Around("@annotation(actionhouse.api.annotations.AuthCheck)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        String email = null;

        for (int i = 0; i < parameterNames.length; i++) {
            if ("email".equals(parameterNames[i]) && args[i] instanceof String) {
                email = (String) args[i];
            }
        }

        if(email == null || customerRepository.findByEmail(email).isEmpty()) {
            throw new AuthorizationException("Login for user `%s` is invalid.".formatted(email));
        }

        return joinPoint.proceed();
    }
}
