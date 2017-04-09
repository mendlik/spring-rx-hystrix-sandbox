package com.sandbox.examples;

import static java.util.Objects.requireNonNull;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sandbox.concurrent.ThreadReport;
import com.sandbox.concurrent.ThreadTracer;
import rx.Scheduler;
import rx.Single;

class RxHystrixThreadReporter {

  private final Scheduler scheduler;
  private final HystrixThreadReporter hystrixThreadReporter;

  RxHystrixThreadReporter(Scheduler scheduler, HystrixThreadReporter hystrixThreadReporter) {
    this.scheduler = requireNonNull(scheduler);
    this.hystrixThreadReporter = requireNonNull(hystrixThreadReporter);
  }

  @HystrixCommand(commandKey = "RxHystrixThreadReporter_generateThreadReport")
  Single<ThreadReport> generateThreadReport(ThreadTracer threadTracer) {
    return Single.fromCallable(threadTracer::registerCurrentThread)
        .map(hystrixThreadReporter::generateThreadReport)
        .subscribeOn(scheduler);
  }
}
