package fa.appcode.common.logging;

import java.util.Arrays;
import java.io.File;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Value("${logging.file.name:logs/kindergarten.log}")
    private String logFilePath;

    @PostConstruct
    public void init() {
        // Ensure log directory exists
        try {
            // Handle both forward and backslash path separators
            String directory;
            if (logFilePath.contains("/")) {
                directory = logFilePath.substring(0, logFilePath.lastIndexOf('/'));
            } else if (logFilePath.contains("\\")) {
                directory = logFilePath.substring(0, logFilePath.lastIndexOf('\\'));
            } else {
                directory = "logs"; // Default directory
            }
            
            File logDir = new File(directory);
            if (logDir.mkdirs()) {
                log.info("Created log directory: {}", logDir.getAbsolutePath());
            }
            log.info("Logging system initialized. Logs will be written to: {}", new File(logFilePath).getAbsolutePath());
            log.debug("Debug logging is enabled");
            log.warn("This is a test warning message");
            log.error("This is a test error message");
        } catch (Exception e) {
            log.error("Failed to initialize logging directory", e);
            System.err.println("Failed to initialize logging directory: " + e.getMessage());
        }
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(fa.appcode..*)" +
            " || within(fa.appcode.services..*)" +
            " || within(fa.appcode.repositories..*)" +
            " || within(fa.appcode.security..*)" +
            " || within(fa.appcode.exceptions..*)" +
            " || within(fa.appcode.web.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
        if (e.getMessage() != null) {
            log.error("Exception message: {}", e.getMessage());
        }
        // Log stack trace for better debugging
        log.debug("Exception stack trace:", e);
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
