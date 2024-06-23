package com.xiaomi.aspect;

import com.xiaomi.service.WarnService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class DataResetAspect {

    private final WarnService warnService;
    private final TaskScheduler taskScheduler;

    private static int writeCnt = 0;

    // 定义切点,匹配所有数据库写操作
    @Pointcut("execution(* com.xiaomi.service.*.insert*(..)) || " +
              "execution(* com.xiaomi.service.*.update*(..)) || " +
              "execution(* com.xiaomi.service.*.delete*(..)) || " +
    "execution(* com.xiaomi.service.impl.WarnServiceImpl.warn(..))")
    public void writeMethods() {
    }

    @AfterReturning("writeMethods()")
    public void afterDataModificationMethods(JoinPoint joinPoint) {
        if(writeCnt == 0){
            // 设置定时任务在10分钟后还原数据
            taskScheduler.schedule(() -> {
                warnService.resetData();
                writeCnt = 0;
            }, new Date(System.currentTimeMillis() + 10 * 60 * 1000));
        } else if (writeCnt > 1000){
            // 超过一定次数的写操作,立即重置数据
            warnService.resetData();
            writeCnt = 0;
        }
        writeCnt ++;
    }
}
