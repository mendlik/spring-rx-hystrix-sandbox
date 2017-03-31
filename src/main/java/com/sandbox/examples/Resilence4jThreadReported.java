package com.sandbox.examples;

import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import io.github.robwin.circuitbreaker.CircuitBreaker;

import java.util.concurrent.Callable;

class Resilence4jThreadReported {

    ThreadReport generateThreadReport(ThreadTracer threadTracer) throws Exception {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("Resilence4jThreadReported");
        Callable<ThreadReport> threadReportCallable = CircuitBreaker.decorateCallable(circuitBreaker, () -> threadTracer
                .registerCurrentThread()
                .generateThreadReport());
        return threadReportCallable.call();
    }
}
