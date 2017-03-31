package com.sandbox.examples;

import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static java.util.Objects.requireNonNull;

class Rx2ThreadReporter {
    private final Scheduler scheduler;

    Rx2ThreadReporter(Scheduler scheduler) {
        this.scheduler = requireNonNull(scheduler);
    }

    Observable<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
        return Observable.fromCallable(threadTracer::registerCurrentThread)
            .map(ThreadTracer::generateThreadReport)
            .subscribeOn(scheduler);
    }
}
