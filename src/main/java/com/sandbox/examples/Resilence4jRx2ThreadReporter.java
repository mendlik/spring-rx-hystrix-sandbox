package com.sandbox.examples;

import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import io.github.robwin.circuitbreaker.CircuitBreaker;
import io.github.robwin.circuitbreaker.operator.CircuitBreakerOperator;
import io.reactivex.Observable;

import static java.util.Objects.requireNonNull;

class Resilence4jRx2ThreadReporter {
    private final Rx2ThreadReporter rx2ThreadReporter;

    Resilence4jRx2ThreadReporter(Rx2ThreadReporter rx2ThreadReporter) {
        this.rx2ThreadReporter = requireNonNull(rx2ThreadReporter);
    }

    Observable<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("Resilence4jRx2ThreadReporter");
        return rx2ThreadReporter
                .generateThreadReport(threadTracer.registerCurrentThread())
                .lift(CircuitBreakerOperator.of(circuitBreaker));
    }
}
