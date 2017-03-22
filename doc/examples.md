# Included examples

## Undertow thread

```sh
curl -s "http://localhost:8080/threads/undertow" | jq '.threadTrace'
[
  "XNIO-2 task-40 Id=102 RUNNABLE"
]
```

Observations: XNIO belongs to Undertow. Action is executed on Undertows thread.
Conclusion: Undertow thread is used to execute an action. Servlet may become unresponsive.

## Rx thread
```sh
curl -s "http://localhost:8080/threads/rx" | jq '.threadTrace'
[
  "XNIO-2 task-41 Id=103 RUNNABLE",
  "RxIoScheduler-2 Id=35 RUNNABLE"
]
```
Observations: RxIoScheduler belongs to JavaRx. Parent thread is not blocked. Action is executed on Rx thread.
Conclusion: Rx is good for separating thread pools

## Hystrix thread

```sh
curl -s "http://localhost:8080/threads/hystrix" | jq '.threadTrace'
[
  "XNIO-2 task-43 Id=105 WAITING on java.util.concurrent.CountDownLatch$Sync@449b840f",
  "hystrix-HystrixThreadReporter-10 Id=90 RUNNABLE"
]
```

Observations: hystrix-HystrixThreadReporter belongs to Hystrix. It blocks parent thread.
Conclusion: Hystrix is not good for separating thread pools. It's good for dealing with CB patterns, fallbacks and timeouts

## Rx + Hystrix

```sh
curl -s "http://localhost:8080/threads/rxhystrix" | jq '.threadTrace'
[
  "XNIO-2 task-44 Id=106 WAITING on java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject@75f34f8a",
  "RxIoScheduler-2 Id=35 WAITING on java.util.concurrent.CountDownLatch$Sync@4a62eb2a",
  "hystrix-HystrixThreadReporter-10 Id=90 RUNNABLE"
]
```
Observations: Undertow thread and Rx thread are blocked. Action is executed on a Hystrix thread. We need two threads to handle a request.
Conclusion: Hystrix creates a separate thread and do not reuse rx thread in this case.

## Hystrix + Rx


```sh
curl -s "http://localhost:8080/threads/hystrixrx" | jq '.threadTrace'
[
  "XNIO-2 task-46 Id=108 RUNNABLE",
  "RxIoScheduler-2 Id=35 RUNNABLE"
]
```

Observations: None thread is blocked. Action is executed on a rx thread. Hystrix reuses rx thread.
Conclusion: Perfect solution ;)