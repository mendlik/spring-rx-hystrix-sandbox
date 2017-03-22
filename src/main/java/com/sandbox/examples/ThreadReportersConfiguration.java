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
    EmbeddedRxThreadReporter embeddedRxThreadReporter() {
        return new EmbeddedRxThreadReporter(Schedulers.computation(), rxThreadReporter());
    }

}
