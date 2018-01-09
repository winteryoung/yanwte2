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

### Demo

The demo project illustrates a typical scenario, showing how to use Yanwte2 in a spring boot project.

https://github.com/winteryoung/yanwte2/tree/master/yanwte2-demo

## PERFORMANCE

## HOW-TO GUIDE

### Standard combinators

### Unnamed combinator

### Custom combinators

### Debugging

### Spring integration

### Intercepting provider execution

### Data extensions

### Building your plugin system

## HISTORY

Yanwte came out of the need from the inside of Alibaba inc., the trading department. It's online trading systems are using the framework called tradespi, which is the predecessor of Yanwte. 3 years since the inception of tradespi, I felt it's time to rewrite it and open-source it to the community, so that we can share a mature thought of how to extend a complex program to everyone.

After open-sourced our trading systems inside the corporation, the systems could deliver 60+ bug fixes/features a week. The biggest system of them, could deliver 20+ bug fixes/features per week.

## YANWTE V.S. YANWTE2

Yanwte2 rewrites Yanwte completely. The benefit is light. Both in the library itself and the way you use it. There's no startup process. Due to the constraint of the startup process, Yanwte can only be used in terminal applications, not in libraries. But Yanwte2 can be used both in applications and libraries.

Yanwte2 uses the Java standard `Function` interface to represent a SPI. This design decision greatly reduced the need for byte code instrumentation. And the depth of the call stack has been greatly reduced too. This improves the performance.