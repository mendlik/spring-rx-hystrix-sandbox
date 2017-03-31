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
    private final Resilence4jThreadReported resilence4jThreadReported;
    private final Rx2ThreadReporter rx2ThreadReporter;
    private final Resilence4jRx2ThreadReporter resilence4jRx2ThreadReporter;

    public ThreadReportEndpoint(RxThreadReporter rxThreadReporter,
                                EmbeddedRxThreadReporter embeddedRxThreadReporter,
                                HystrixThreadReporter hystrixThreadReporter,
                                RxHystrixThreadReporter rxHystrixThreadReporter,
                                HystrixRxThreadReporter hystrixRxThreadReporter,
                                Resilence4jThreadReported resilence4jThreadReported,
                                Rx2ThreadReporter rx2ThreadReporter,
                                Resilence4jRx2ThreadReporter resilence4jRx2ThreadReporter) {
        this.rxThreadReporter = requireNonNull(rxThreadReporter);
        this.embeddedRxThreadReporter = requireNonNull(embeddedRxThreadReporter);
        this.hystrixThreadReporter = requireNonNull(hystrixThreadReporter);
        this.rxHystrixThreadReporter = requireNonNull(rxHystrixThreadReporter);
        this.hystrixRxThreadReporter = requireNonNull(hystrixRxThreadReporter);
        this.resilence4jThreadReported = requireNonNull(resilence4jThreadReported);
        this.rx2ThreadReporter = requireNonNull(rx2ThreadReporter);
        this.resilence4jRx2ThreadReporter = requireNonNull(resilence4jRx2ThreadReporter);
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

    @GetMapping("/rx2")
    io.reactivex.Observable<ThreadReport> getThreadReportForRxJava2() {
        return rx2ThreadReporter
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

    @GetMapping("/resilence4j")
    ThreadReport getThreadReportForResilence4j() throws Exception {
        return resilence4jThreadReported
                .generateThreadReport(createThreadTrace());
    }

    @GetMapping("/resilence4jrx2")
    io.reactivex.Observable<ThreadReport> getThreadReportForResilence4jRx2() throws Exception {
        return resilence4jRx2ThreadReporter
                .generateThreadReport(createThreadTrace());
    }
}
