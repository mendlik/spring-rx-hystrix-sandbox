package com.sandbox.concurrent;

import static java.lang.management.ManagementFactory.getThreadMXBean;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;

class ThreadDumper {

  static List<ThreadInfo> generateThreadDump() {
    return generateThreadDump(getAllThreadIds());
  }

  static List<ThreadInfo> generateThreadDump(List<Long> threadIds) {
    requireNonNull(threadIds);
    ThreadMXBean threadMXBean = getThreadMXBean();
    return threadIds.stream()
        .map(threadMXBean::getThreadInfo)
        .map(ThreadDumper::toThreadInfo)
        .collect(toList());
  }

  private static ThreadInfo toThreadInfo(java.lang.management.ThreadInfo threadInfo) {
    return new ThreadInfo(
        threadInfo.getThreadId(),
        threadInfo.getThreadName(),
        threadInfo.getThreadState()
    );
  }

  private static List<Long> getAllThreadIds() {
    return Arrays.stream(getThreadMXBean().getAllThreadIds())
        .mapToObj(l -> l)
        .collect(toList());
  }

}
