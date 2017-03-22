# Hystrx + RxJava in nonideal world

## What is the problem?

The problem is a suboptimal way thread pools are managed in Java projects.
Inefficient thread management produces problems with performance (throughput) and memory consumption.

All problems and examples are presented in a sample project [rx-hystrix-sandbox](https://stash.allegrogroup.com/users/pawel.mendelski/repos/rx-hystrix-sandbox/browse).

Further sections presents common misconceptions

### Antipattern 1: Operating on rx.Schedulers.io()

[RxJava](https://github.com/ReactiveX/RxJava) provides some [Schedulers](http://reactivex.io/RxJava/javadoc/rx/schedulers/Schedulers.html) out of the box. Most important ones are:

- [Schedulers.computation()](http://reactivex.io/RxJava/javadoc/rx/schedulers/Schedulers.html#computation()) - that is intended for computational work. It is backed by a bounded thread-pool with size equal to the number of available processors. Such a thread-pool size guarantees that there is one thread per one core and there is no overhead of context switching between them. Examples: text processing, image/voice recognition, algorithms
- [Schedulers.io()](http://reactivex.io/RxJava/javadoc/rx/schedulers/Schedulers.html#io()) - that is intended for IO-bound work. It is backed by an unbounded thread-pool. It's designed for threads that most of the time stay in [waiting state](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#WAITING). Examples: database queries, HTTP calls, interaction with a file system

Although `Schedulers.io()` ius designed for IO work it's not a good idea to use it across your whole application.

- It is an unbounded thread pool that can cause OutOfMemoryError
- It is not measurable. All dependencies are used by the same thread pool. It's hard to tell which dependency causes a problem.

### Antipattern 2: Executing hystrix comamnds from Servlet thread

[Hystrix](https://github.com/Netflix/Hystrix) implements a [Circuit Breaker design pattern](https://www.wikiwand.com/en/Circuit_breaker_design_pattern).

By default Hystrix stops parent thread and runs it self on a thread from a dedicated thread pool. This way Hystrix has full control over the thread used to execute the action.

Unfortunately if the parent thread belongs to a Servlet container. Servlet may bay become unresponsive very quickly. Servlet has one thread pool for all endpoint if one endpoint consume all the threads all endpoints become unresponsive.

### Antipattern 3: Executing hystrix comamnds from an Observable

In many projects the integration of RxJava and Hystrix is implemented in an incorrect way where

- Servlet Thread - waits for Rx thread
- Rx thread - waits for Hystrix thread
- Hystrix thread - executes the action

In such a situation there are 3 threads needed to handle a single request.

It can be easily fixed by a [HystrixObservableCommand](https://github.com/Netflix/Hystrix/wiki/How-To-Use#hystrixobservablecommand-equivalent-2).

## What do we expect from the solution?

The final solution should provide:
- one manageable and measurable thread pool per dependency (database or other service)
- as little time on servlet thread as possible
- circuit breaker pattern and timeout management

## How to achieve it with Hystrix and RxJava?

In order to free Servlet thread use deffered responses. It's easy with SpringMVC:

```java
@RestController
@RequestMapping("/items")
class ItemsEndpoint {
    private final ItemsProvider itemsProvider;

    ItemsEndpoint(ItemsProvider itemsProvider) {
        this.itemsProvider = requireNonNull(itemsProvider);
    }

    @GetMapping('/{id}')
    Single<Item> getItem(@PathVariable String id) {
        return itemsProvider.getItem(id).toSingle();
    }
}
```

Item provider should use [javanica](https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-javanica) to setup HystrixCommand.
Javanica is a library that uses annotations and reflection to configure hystrix.

```java
class ItemsProvider {
    private final Scheduler scheduler;

    ItemsProvider(Scheduler scheduler) {
        this.scheduler = requireNonNull(scheduler);
    }

    @HystrixCommand(commandKey = HystrixCommands.GET_ITEM)
    Observable<Item> getItem(@PathVariable String id) {
        return Observable.just(id)
            .map(this::fetchItem)
            .subscribeOn(scheduler);
    }
}
```

There are few gotchas:
- Hystrix command [must return rx.Observable](https://github.com/Netflix/Hystrix/issues/1403) in order to uses rx thread and not spawn a separate one.
- SpringMVC command must return rx.Single otherwise it will throw a mapping exception.
- Hystrix command key in most cases should be unique across whole application. It should be enforced by an aspect.

Last but not least remember to setup timeout for every hystrix command after context initialization.

```java
@Configuration
public class HystrixTimeoutsConfiguration {

    @Autowired
    void setupHystrixTimeouts(@Value("${itemsProvider.hystrix.timeout}") Integer itemsProviderTimeout) {
        setCommandTimeout(HystrixCommandKeys.GET_ITEM, itemsProviderTimeout);
    }

    private void setCommandTimeout(String commandKey, int timeout) {
        String commandTimeoutProperty = format("hystrix.command.%s.execution.isolation.thread.timeoutInMilliseconds", commandKey);
        ConfigurationManager.getConfigInstance().addProperty(commandTimeoutProperty, timeout);
    }
}
```

Timeouts are setup manually because it's the only way to manage them across different environments. Most often integration tests require longer timeouts than on production. Javanica does not support reading Spring properties.
