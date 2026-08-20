package ru.autotestframework.cucumber.step_executor;

import static ru.autotestframework.util.Validator.exception;

import io.cucumber.java.StepDefinitionAnnotation;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.cucumber.type.resolvable.IResolvable;

/**
 * Class to 1. Proceed Templates Variables. 2. Wrap Retry Steps
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class StepExecutorAspect {

    private final FrameworkProperties properties;
    private final StepExecutor stepExecutor;
    private final PlaceholderResolver placeholderResolver;

    /**
     * Add steps to executor object.
     *
     * @param joinPoint the join point
     * @return the object
     */
    @SneakyThrows
    @Around("execution(@(@io.cucumber.java.StepDefinitionAnnotation *) * *(..)) "
            + "&& !@annotation(ru.autotestframework.cucumber.step_executor.DontAddToStepExecutor)")
    public Object addStepsToExecutor(final ProceedingJoinPoint joinPoint) {
        validateStep(joinPoint);
        if (stepExecutor.isEnabled()) {
            stepExecutor.addStep(wrapToRunnable(joinPoint).setArgs(joinPoint.getArgs()));
            return null;
        } else {
            return joinPoint.proceed(resolveArgs(joinPoint.getArgs()));
        }
    }

    private Object[] resolveArgs(final Object[] args) {
        var resolveArgs = new Object[args.length];
        for (var i = 0; i <= args.length - 1; i++) {
            resolveArgs[i] = extractArg(args[i]);
        }
        return resolveArgs;
    }

    private Object extractArg(final Object arg) {
        if (IResolvable.class.isAssignableFrom(arg.getClass())) {
            return ((IResolvable) arg).resolve(arg);
        } else if (String.class.isAssignableFrom(arg.getClass())) {
            return placeholderResolver.resolve(arg.toString());
        } else {
            return arg;
        }
    }

    private void validateStep(final ProceedingJoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        var deprecatedOpt = Optional.ofNullable(method.getAnnotation(Deprecated.class));

        if (deprecatedOpt.isPresent()) {
            throw exception("The Step is deprecated, appropriate new Step should be used instead");
        }
    }

    private StepRunnable wrapToRunnable(final ProceedingJoinPoint joinPoint) {
        return new StepRunnable() {
            private Object[] args;

            @Override
            public String getStepExpression() {
                var signature = (MethodSignature) joinPoint.getSignature();
                return StepExecutorAspect.getStepExpression(signature);
            }

            public StepRunnable setArgs(final Object[] args) {
                this.args = args;
                return this;
            }

            @SneakyThrows
            @Override
            public void run() {
                try {
                    joinPoint.proceed(resolveArgs(args));
                } catch (Throwable throwable) {
                    throw throwable;
                }
            }
        };
    }

    private static String getStepExpression(final MethodSignature signature) {
        try {
            var method = signature.getMethod();
            for (var annotation : method.getAnnotations()) {
                if (isStepDefinitionAnnotation(annotation)) {
                    var expressionMethod = annotation.getClass().getMethod("value");
                    return (String) expressionMethod.invoke(annotation);
                }
            }
            log.error("Ошибка получения имени шага");
            return "error";
        } catch (Exception exception) {
            log.error("Ошибка получения имени шага", exception);
            return "error";
        }
    }

    private static boolean isStepDefinitionAnnotation(final Annotation annotation) {
        return annotation.annotationType().isAnnotationPresent(StepDefinitionAnnotation.class);
    }
}
