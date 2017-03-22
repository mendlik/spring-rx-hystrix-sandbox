package com.sandbox.examples;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import rx.Observable;
import rx.Single;

import static java.util.Objects.requireNonNull;

class HystrixRxThreadReporter {
    private final RxThreadReporter rxThreadReporter;

    HystrixRxThreadReporter(RxThreadReporter rxThreadReporter) {
        this.rxThreadReporter = requireNonNull(rxThreadReporter);
    }

    @HystrixCommand(commandKey = "HystrixRxThreadReporter_generateThreadReport")
    Observable<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
        return rxThreadReporter
            .generateThreadReport(threadTracer.registerCurrentThread())
            .toObservable();
    }

    /**
     * Look out: rx.Single will not be treated as a reactive stream by Hystrix!
     * Issue: https://github.com/Netflix/Hystrix/issues/1403
     * @see {@link com.netflix.hystrix.contrib.javanica.command.ExecutionType}
     * @param threadTracer
     * @return
     */
    @HystrixCommand(commandKey = "HystrixRxThreadReporter_generateThreadReportSingle")
    Single<ThreadReport> generateThreadReportSingle(ThreadTracer threadTracer) {
        return rxThreadReporter
            .generateThreadReport(threadTracer.registerCurrentThread());
    }
}
