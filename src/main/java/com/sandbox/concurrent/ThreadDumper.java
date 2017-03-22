package com.sandbox.concurrent;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;

import static java.lang.management.ManagementFactory.getThreadMXBean;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

class ThreadDumper {

    static List<String> generateThreadDump() {
        return generateThreadDump(getAllThreadIds());
    }

    static List<String> generateThreadDump(List<Long> threadIds) {
        requireNonNull(threadIds);
        ThreadMXBean threadMXBean = getThreadMXBean();
        return threadIds.stream()
            .map(id -> threadMXBean.getThreadInfo(id, 5))
            .map(ThreadInfo::toString)
            .map(String::trim)
            .map(dump -> dump.replaceAll("\"", ""))
            .collect(toList());
    }

    private static List<Long> getAllThreadIds() {
        return Arrays.stream(getThreadMXBean().getAllThreadIds())
            .mapToObj(l -> l)
            .collect(toList());
    }

}
