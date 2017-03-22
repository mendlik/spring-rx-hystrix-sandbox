package com.sandbox.examples;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;

class HystrixThreadReporter {
    @HystrixCommand
    ThreadReport generateThreadReport(ThreadTracer threadTracer) {
        return threadTracer
            .registerCurrentThread()
            .generateThreadReport();
    }
}
