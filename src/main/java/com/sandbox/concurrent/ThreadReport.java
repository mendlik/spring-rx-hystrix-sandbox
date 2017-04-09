package com.sandbox.concurrent;

import java.util.List;

public class ThreadReport {

  private final List<ThreadInfo> threadTrace;
  private final List<ThreadInfo> threadDump;

  ThreadReport(List<ThreadInfo> threadTrace, List<ThreadInfo> threadDump) {
    this.threadTrace = threadTrace;
    this.threadDump = threadDump;
  }

  public List<ThreadInfo> getThreadTrace() {
    return threadTrace;
  }

  public List<ThreadInfo> getThreadDump() {
    return threadDump;
  }
}
