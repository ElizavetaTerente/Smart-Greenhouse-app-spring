package at.qe.skeleton.model;

import at.qe.skeleton.services.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Pointcut("execution(* createUser(Userx))")
    public void createUserPointcut() {}

    @Pointcut("execution(* updateUserInfo(Userx, java.util.Set, String, String, String, String))")
    public void updateUserPointcut() {}

    @Pointcut("execution(* deleteUser(Userx))")
    public void deleteUserPointcut() {}

    @Pointcut("execution(* createAccesspoint(Accesspoint))")
    public void createAccesspointPointcut() {}


    @AfterReturning(pointcut = "createUserPointcut()")
    public void logCreateUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Userx correspondingObject = (Userx) args[0];
        auditLogService.logEvent(correspondingObject.getCreateUser().getUsername(), "createUser", correspondingObject.toString());
    }

    @AfterReturning(pointcut = "updateUserPointcut()")
    public void logUpdateUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Userx correspondingObject = (Userx) args[0];
        auditLogService.logEvent(correspondingObject.getUpdateUser().getUsername(), "updateUser", correspondingObject.toString());
    }

    @AfterReturning(pointcut = "deleteUserPointcut()")
    public void logDeleteUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Userx correspondingObject = (Userx) args[0];
        auditLogService.logEvent(correspondingObject.getUpdateUser().getUsername(), "deleteUser", correspondingObject.toString());
    }

    @AfterReturning(pointcut = "createAccesspointPointcut()")
    public void logCreateAccesspoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Accesspoint correspondingObject = (Accesspoint) args[0];
        auditLogService.logEvent(correspondingObject.getCreateUser().getUsername(), "createAccesspoint", correspondingObject.toString());
    }

    //TODO:  Ausfälle der Sensorstationen, Accesspoints oder des Webservers loggen
}
