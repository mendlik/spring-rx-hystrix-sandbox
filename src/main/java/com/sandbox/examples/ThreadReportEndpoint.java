package com.sandbox.examples;

import com.sandbox.concurrent.ThreadReport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Single;

import static com.sandbox.concurrent.ThreadTracer.createThreadTrace;
import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/threads")
class ThreadReportEndpoint {
    private final RxThreadReporter rxThreadReporter;
    private final EmbeddedRxThreadReporter embeddedRxThreadReporter;
    private final HystrixThreadReporter hystrixThreadReporter;
    private final RxHystrixThreadReporter rxHystrixThreadReporter;
    private final HystrixRxThreadReporter hystrixRxThreadReporter;

    public ThreadReportEndpoint(RxThreadReporter rxThreadReporter,
                                EmbeddedRxThreadReporter embeddedRxThreadReporter,
                                HystrixThreadReporter hystrixThreadReporter,
                                RxHystrixThreadReporter rxHystrixThreadReporter,
                                HystrixRxThreadReporter hystrixRxThreadReporter) {
        this.rxThreadReporter = requireNonNull(rxThreadReporter);
        this.embeddedRxThreadReporter = requireNonNull(embeddedRxThreadReporter);
        this.hystrixThreadReporter = requireNonNull(hystrixThreadReporter);
        this.rxHystrixThreadReporter = requireNonNull(rxHystrixThreadReporter);
        this.hystrixRxThreadReporter = requireNonNull(hystrixRxThreadReporter);
    }

    @GetMapping("/undertow")
    ThreadReport getThreadReport() {
        return createThreadTrace()
            .generateThreadReport();
    }

    @GetMapping("/rx")
    Single<ThreadReport> getThreadReportForRxJava() {
        return rxThreadReporter
            .generateThreadReport(createThreadTrace());
    }

    @GetMapping("/rxembedded")
    Single<ThreadReport> getThreadReportForRxJavaWithCustomScheduler() {
        return embeddedRxThreadReporter
            .generateThreadReport(createThreadTrace());
    }

    @GetMapping("/hystrix")
    ThreadReport getThreadReportForHytrix() {
        return hystrixThreadReporter
            .generateThreadReport(createThreadTrace());
    }

    @GetMapping("/hystrixrx")
    Single<ThreadReport> getThreadReportForHystrixWithRxJava() {
        return hystrixRxThreadReporter
            .generateThreadReport(createThreadTrace())
            .toSingle();
    }

    @GetMapping("/hystrixrx/single")
    Single<ThreadReport> getThreadReportForHystrixWithRxJavaThatOperatesOnSingle() {
        return hystrixRxThreadReporter
            .generateThreadReportSingle(createThreadTrace());
    }

    @GetMapping("/rxhystrix")
    Single<ThreadReport> getThreadReportForRxJavaWithHystrix() {
        return rxHystrixThreadReporter
            .generateThreadReport(createThreadTrace());
    }
}
