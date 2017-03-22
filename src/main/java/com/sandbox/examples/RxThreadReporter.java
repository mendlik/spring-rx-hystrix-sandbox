package com.sandbox.examples;

import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import rx.Scheduler;
import rx.Single;

import static java.util.Objects.requireNonNull;

class RxThreadReporter {
    private final Scheduler scheduler;

    RxThreadReporter(Scheduler scheduler) {
        this.scheduler = requireNonNull(scheduler);
    }

    Single<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
        return Single.fromCallable(threadTracer::registerCurrentThread)
            .map(ThreadTracer::generateThreadReport)
            .subscribeOn(scheduler);
    }
}
