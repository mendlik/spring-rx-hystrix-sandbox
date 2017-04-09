package com.sandbox.examples;

import static java.util.Objects.requireNonNull;

import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import rx.Scheduler;
import rx.Single;

class EmbeddedRxThreadReporter {

  private final Scheduler scheduler;
  private final RxThreadReporter rxThreadReporter;

  EmbeddedRxThreadReporter(Scheduler scheduler, RxThreadReporter rxThreadReporter) {
    this.scheduler = requireNonNull(scheduler);
    this.rxThreadReporter = requireNonNull(rxThreadReporter);
  }

  Single<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
    return Single.fromCallable(threadTracer::registerCurrentThread)
        .flatMap(rxThreadReporter::generateThreadReport)
        .subscribeOn(scheduler);
  }
}
