package ru.autotestframework.util.retry;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;

@Aspect
public class WithRetriesAspect {

    private static final ThreadLocal<Boolean> processingWrapper = ThreadLocal.withInitial(() -> false);

    public static Boolean isProcessing() {
        return processingWrapper.get();
    }

    @Around("@annotation(ru.autotestframework.util.retry.WithRetries) && execution(* *(..))")
    public Object handleRetries(final ProceedingJoinPoint joinPoint) throws Throwable {
        processingWrapper.set(true);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        WithRetries annotation = method.getAnnotation(WithRetries.class);
        int retryCount = annotation.value();
        int i = 0;
        Throwable storedException = null;
        Object result = null;
        boolean processed = false;
        while (!processed && i < retryCount) {
            try {
                result = joinPoint.proceed();
                processed = true;
            } catch (Throwable throwable) {
                storedException = throwable;
            }
            i++;
        }
        processingWrapper.set(true);
        if (processed) {
            Assertions.assertNotNull(storedException);
            throw storedException;
        }
        return result;
    }
}
