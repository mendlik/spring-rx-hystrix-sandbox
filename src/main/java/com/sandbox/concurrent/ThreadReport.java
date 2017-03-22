package com.sandbox.concurrent;

import java.util.List;

public class ThreadReport {
    private final List<String> threadTrace;
    private final List<String> threadDump;

    ThreadReport(List<String> threadTrace, List<String> threadDump) {
        this.threadTrace = threadTrace;
        this.threadDump = threadDump;
    }

    public List<String> getThreadTrace() {
        return threadTrace;
    }

    public List<String> getThreadDump() {
        return threadDump;
    }
}
