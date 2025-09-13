package com.frank.messaging.aspect;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

@Component
@Aspect
@Log4j2
public class LoggingAspect {

    @Autowired private CloudWatchClient cloudWatchClient;

    // visibility/observability = metrics + log + alarm
    // metrio
    @Around("execution(* com.frank.messaging.controller.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean isExceptionThrown = false;
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            isExceptionThrown = true;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();

            List<Dimension> dimensionList = new ArrayList<>();
            dimensionList.add(Dimension.builder()
                                      .name("API")
                                      .value(joinPoint.getTarget().getClass().getName() + "." +
                                                     joinPoint.getSignature().getName())
                                      .build());
            PutMetricDataRequest putMetricDataRequest = PutMetricDataRequest.builder()
                    .namespace("MessagingApplication")
                    .metricData(
                            MetricDatum.builder()
                                    .metricName("Latency")
                                    .unit(StandardUnit.MILLISECONDS)
                                    .value((double) (endTime - startTime))
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Invocation")
                                    .value(1.0)
                                    .unit(StandardUnit.COUNT)
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("IsExceptionThrown")
                                    .unit(StandardUnit.COUNT)
                                    .value(isExceptionThrown ? 1.0 : 0.0)
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Latency")
                                    .dimensions(dimensionList)
                                    .unit(StandardUnit.MILLISECONDS)
                                    .value((double) (endTime - startTime))
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Invocation")
                                    .dimensions(dimensionList)
                                    .value(1.0)
                                    .unit(StandardUnit.COUNT)
                                    .build(),
                            MetricDatum.builder()
                                    .dimensions(dimensionList)
                                    .metricName("IsExceptionThrown")
                                    .unit(StandardUnit.COUNT)
                                    .value(isExceptionThrown ? 1.0 : 0.0)
                                    .build())
                    .build();
            cloudWatchClient.putMetricData(putMetricDataRequest);

            log.info("Class: {} Method: {} Latency: {} ms. IsExceptionThrown: {}",
                     joinPoint.getTarget().getClass().getName(),
                     joinPoint.getSignature().getName(),
                     endTime - startTime,
                     isExceptionThrown);
        }
    }

    @Before("execution(* com.frank.messaging.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Starting invocation of Class: {} Method: {}",
                 joinPoint.getTarget().getClass().getName(),
                 joinPoint.getSignature().getName());
    }

}
