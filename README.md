# Yanwte2

Yanwte [ˈyænti] stands for yet another way to extend. It's a library to help you extending your programs. This library is written in Java 8.

## LICENSE

Apache license 2.0. You are free to use in any which way.

## INTRODUCTION

Briefly, Yanwte2 is a way to use SPI (Service Provider Interface). The standard Java method to use a SPI is to use `ServiceLoader` to get an enumeration of the providers.

 The problem with that is you don't know what to do with the providers. There must be some relations between them. Are they mutually exclusive? Can they be used together? If so, how to combine their results?
 
 Yanwte2's solution to this problem is to define a tree to orchestrate those providers. There are two important relation types: chain and map reduce. They are expressed as nodes in the orchestrator tree.
 
 A chain node is like a chain of responsibilities. Any provider in the chain can decide when to break the chain. This node basically says the providers are mutually exclusive. Or if no provider breaks the chain, the the chain expresses the characteristics of a foreach loop.
 
 A map reduce node, as the name suggests, accepts a list of providers, and a reduce function, to produce a result. It basically says the providers can coexist. But you need to coordinate their results.
 
 Based on this basic feature, Yanwte2 can provide a standard way to build a plugin.

## QUICK START

Maven:
```xml
<dependency>
    <groupId>com.github.winteryoung</groupId>
    <artifactId>yanwte2-spring</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Suppose we're writing a Spring boot program that outputs some text, given some number. There are two parts involved: one that defines how to calculate, and one that defines how to format.

```java
public interface NumberProcessor
        extends Function<Integer, Integer>, SpringServiceOrchestrator<NumberProcessor> {
    @Override
    default Combinator tree() {
        return chain(dynamicProviders());
    }
}
```

```java
public interface NumberFormatter
        extends Function<Integer, String>, SpringServiceOrchestrator<NumberFormatter> {
    @Override
    default Combinator tree() {
        return chain(dynamicProviders());
    }
}
```

These two interfaces both accept dynamic providers that can be found at runtime. So that the orchestrators don't need to know the specific implementations.

The providers are organized in a chain node. That way, any provider can decide if it can handle the input and breaks the chain. It's essentially a chain of responsibilities. We will discuss more on combinators later.

Put aside the providers, first we will see how to use the interfaces.

```java
@RestController
public class DemoController {
    private NumberProcessor numberProcessor =
            ServiceOrchestrator.getOrchestrator(NumberProcessor.class);

    private NumberFormatter numberFormatter =
            ServiceOrchestrator.getOrchestrator(NumberFormatter.class);

    @RequestMapping("/demo")
    public String demo(Integer num) {
        Integer i = numberProcessor.apply(num);
        return numberFormatter.apply(i);
    }
}
```

Here we only use the interfaces. That's what we call programming to interfaces. Now we add a series of providers to handle even numbers.

```java
@Service
public class EvenNumberProcessor implements NumberProcessor {
    @Override
    public Integer apply(Integer i) {
        if (i != null && i % 2 == 0) {
            return i * 2;
        }

        // we cannot deal with it, let others do the work
        return null;
    }
}
```

```java
@Service
public class EvenNumberFormatter implements NumberFormatter {
    @Override
    public String apply(Integer num) {
        if (num != null && num % 2 == 0) {
            return "Even " + num + "\n";
        }
        return null;
    }
}
```

With the Spring `@Service` annotation, these providers are registered as Spring beans. So that you can inject any dependencies from the Spring container.

The processor cannot process odd numbers. So when encountered odd numbers, it returns `null`. `null` is special value that tells the chain combinator to proceed to the next one, that it cannot handle the current request.

Now start the application (the default server port is 8080) and test the URL:

```
> curl http://localhost:8080/demo?num=6
Even 12
```

Now add the providers to handle odd numbers.

```java
@Service
public class OddNumberProcessor implements NumberProcessor {
    @Override
    public Integer apply(Integer i) {
        if (i != null && i % 2 != 0) {
            return i * 2 + 1;
        }

        // we cannot deal with it, let others do the work
        return null;
    }
}
```

```java
@Service
public class OddNumberFormatter implements NumberFormatter {
    @Override
    public String apply(Integer num) {
        if (num != null && num % 2 != 0) {
            return "Odd " + num + "\n";
        }
        return null;
    }
}
```

Now the pattern emerges, the odd series form a plugin and the even series form another plugin. We can just package each provider series into separate projects. The provider discovery mechanism depends on Spring.

Non-spring projects are supported, but undocumented. If you have the need, leave an issue.

The above example can be found here:

https://github.com/winteryoung/yanwte2/tree/master/yanwte2-demo

## PERFORMANCE

Service orchestrators are generated dynamically using byte code instrumentation. They are essentially proxies that delegate calls to the combinator tree. It's 5 times faster than reflection. However, still much slower than regular method calls. Currently the byte code instrumentation is done via ByteBuddy, which involves creating a class instance each time an invocation occur. The performance could be improved by hand written those byte code. If you have the need, leave an issue.

## REFERENCES

### Standard combinators

Standard combinators include the following:

* `provider`
  Accepts an argument of the class of the provider. Providers constructed this way must have a parameter-less constructor, and cannot have dependency injection.
* `springProvider`
  Accepts an string specifying the name of the provider bean. You can explicitly specify the name of the bean like `@Service("mybean")`, and use it like `springProvider("mybean")`. Providers constructed this way can have dependency injection.
* `dynamic`
  The above methods introduce a way to statically specify which provider to use. If you are building a plugin system, you won't know the exact providers that will be registered later. `dynamic` combinator will be expanded into a list of providers at runtime. One consequence is that `dynamic` combinator cannot be used at the root of the tree. Because it's unclear what you want to with the multiple results returned by the providers.
* `chain`
  As demonstrated above, `chain` returns the result of the first child that returns non-null. It has the effect of short-circuit. Thus it pretty much the chain of responsibility. If no child returns null, `chain` acts like foreach.
* `mapReduce`
  Accepts a series of combinators and a function to specify how to combine the results returned by the combinators. The request is broadcasted to all the combinators sequentially.
  
The combinator system is recursive. It's possible to build a multi-level tree like the following:

```
mapReduce((String a, String b) -> a + b,
  chain(
    springProvider("bean1"),
    dynamicProviders()
  ),
  springProvider("bean2")  
)
```

### Custom combinators

How ever the standard combinators can be limited if your program is extremely complex. For example, if you want a `decorate` combinator. You can implement one.

```java
class DecorateCombinator implements Combinator {
    ...
}
```

```java
interface CustomServiceOrchestrator extends SpringServiceOrchestrator {
    Combinator <R> decorate(Function<R, R> function, Combinator... combinators) {
        return new DecorateCombinator(function, combinators);
    }
}
```

Inherit from your `CustomServiceOrchestrator` instead of `SpringServiceOrchestrator`. Now you can use the `decorate` combinator.

### Debugging

When using the dynamic combinator, it's possible that you are not surely which providers are loaded in which order. `com.github.winteryoung.yanwte2.core.ServiceOrchestrator.getExpandedTree` can be used to dynamically retrieve this information.

### Intercepting provider execution

Currently there's only one way: define your base class for every providers in your application. If you need the callback API, leave an issue. This can be useful to do universal logging.

### Data extensions

If the program is complex enough, you may need a context to flow around the SPIs (as a parameter). Different plugins may need different extensions to the context. For example, in a e-commerce system, an order can be viewed as the context. An e-book order may have some extra fields, and a order of a chair may be some other extra fields.

```java
class Order implements ExtensibleData { ... }
```
```java
class EbookOrder { ... }
```
```java
class ChairOrder { ... }
```

There's no inheritance relationship. We use composition. You can get your data extension from the context in your provider. For example,

```java
class EbookOrderProcessor implements OrderProcessor {
    @Override
    public Void apply(Order order) {
        EbookOrder ebookOrder = order.getDataExt();
        // do something with ebookOrder
        return null;
    }
}
```

Where did `ebookOrder` come from? We can set the instance via `order.setDataExt()` in some other providers, or we can define a data extension initializer.

Set data extension directly:

```java
class EbookOrderPreProcessor implements OrderPreProcessor {
    @Override
    public Void apply(Order order) {
        order.setDataExt(new EbookOrder());
    }
}
```

Initialize data extension via initializer:

```java
class EbookOrderInitializer implements DataExtensionInitializer<Order, EbookOrder> {
    @Override
    public EbookOrder createDataExtension(Order order) {
        return new EbookOrder();
    }
}
```

## HISTORY

Yanwte came out of the need from the inside of Alibaba inc., the trading department. It's online trading systems are using the framework called tradespi, which is the predecessor of Yanwte. 3 years since the inception of tradespi, I felt it's time to rewrite it and open-source it to the community, so that we can share a mature thought of how to extend a complex program to everyone.

After open-sourced our trading systems inside the corporation, the systems could deliver 60+ bug fixes/features a week. The biggest system of them, could deliver 20+ bug fixes/features per week.

## YANWTE V.S. YANWTE2

Yanwte2 rewrites Yanwte completely. The benefit is light. Both in the library itself and the way you use it. There's no startup process. Due to the constraint of the startup process, Yanwte can only be used in terminal applications, not in libraries. But Yanwte2 can be used both in applications and libraries.

Yanwte2 uses the Java standard `Function` interface to represent a SPI. This design decision greatly reduced the need for byte code instrumentation. And the depth of the call stack has been greatly reduced too. This improves the performance.