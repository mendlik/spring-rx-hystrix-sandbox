package com.sandbox.examples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Configuration
class ThreadReportersConfiguration {
    @Bean
    HystrixRxThreadReporter hystrixRxThreadReporter() {
        return new HystrixRxThreadReporter(rxThreadReporter());
    }

    @Bean
    RxThreadReporter rxThreadReporter() {
        return new RxThreadReporter(ioScheduler());
    }

    @Bean
    Rx2ThreadReporter rx2ThreadReporter() {
        return new Rx2ThreadReporter(io.reactivex.schedulers.Schedulers.io());
    }

    @Bean
    Scheduler ioScheduler() {
        return Schedulers.io();
    }

    @Bean
    RxHystrixThreadReporter rxHystrixThreadReporter() {
        return new RxHystrixThreadReporter(ioScheduler(), hystrixThreadReporter());
    }

    @Bean
    HystrixThreadReporter hystrixThreadReporter() {
        return new HystrixThreadReporter();
    }

    @Bean
    Resilence4jThreadReported resilence4jThreadReported() {
        return new Resilence4jThreadReported();
    }

    @Bean
    Resilence4jRx2ThreadReporter resilence4jRx2ThreadReporter() {
        return new Resilence4jRx2ThreadReporter(rx2ThreadReporter());
    }

    @Bean
    EmbeddedRxThreadReporter embeddedRxThreadReporter() {
        return new EmbeddedRxThreadReporter(Schedulers.computation(), rxThreadReporter());
    }

}
