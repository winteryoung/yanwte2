# Yanwte2

Yanwte [ˈyænti] stands for yet another way to extend. It's a library to help you extending your programs. This library is written in Java 8.

## LICENSE

Apache license 2.0. You are free to use in any which way.

## INTRODUCTION

Briefly, Yanwte2 is a way to use SPI. The standard Java method to use a SPI is to use `ServiceLoader` to get an enumeration of the providers.

 The problem with that is you don't know what to do with the providers. There must be some relations between them. Are they mutually exclusive? Can they be used together? If so, how to combine their results?
 
 Yanwte2's solution to this problem is to define a tree to define the relations among the providers. There are two important relation types: chain and map reduce. They are expressed as nodes in the tree.
 
 A chain node is like a chain of responsibilities. Any provider in the chain can decide when to break the chain. This node basically says the providers are mutually exclusive. Or if no provider breaks the chain, the the chain expresses the characteristics of a foreach loop.
 
 A map reduce node, as the name suggests, accepts a list of providers, and a reduce function, to produce a result. It basically says the providers can coexist. But you need to coordinate their results.
 
 Based on this basic feature, Yanwte2 can provide a standard way to build a plugin.

## QUICK START

Maven:
```xml
<dependency>
    <groupId>com.github.winteryoung</groupId>
    <artifactId>yanwte2</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## PERFORMANCE

## HOW-TO GUIDE

### Standard combinators

### Unnamed combinator
