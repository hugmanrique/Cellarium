# :file_folder: Cellarium

[![jitpack][jitpack]][jitpack-url]
[![tests][tests]][tests-url]
[![license][license]][license-url]

**Cellarium** is a typesafe heterogeneous container API. Unlike a regular map, the [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) _keys_ are parameterized instead of the map.
This means that all the keys are of different types, allowing a [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) instance to hold values of many (i.e. heterogeneous) types.
This type token is used to guarantee that the type of a value agrees with its [`Key`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Key.html).

This is a really powerful abstraction that can, for example, model database rows (that can have arbitrarily many columns of different types), user settings, player statistics...

This project started as an exercise implementation of Joshua Bloch's [_Effective Java_, 3rd edition](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/) Item 33: Consider typesafe heterogeneous containers.
In the process, I designed a more flexible and capable API and decided to open source it.

## Getting started

You can install Cellarium using [Maven](https://maven.apache.org/) by adding the JitPack repository to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Next, add the `Cellarium` dependency:

```xml
<dependency>
    <groupId>com.github.hugmanrique</groupId>
    <artifactId>Cellarium</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

You will need to have Java 8 or later.

## Usage

As an example, we're going to create a statistics [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) that allows its clients to store and retrieve the value of any given statistic by its [`Key`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Key.html).

First, let's add the statistics [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) to the `Player` class:

```java
public class Player {
    private final Repository statistics;

    public Player() {
        // ...
        this.statistics = SimpleRepository.newInstance();
    }
}
```

> The default `SimpleRepository` is not thread-safe. Check the javadocs on [`SimpleRepository.newConcurrentInstance()`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/simple/SimpleRepository.html#newConcurrentInstance()) to see how to create a thread-safe `Repository`.

Next, let's create the chess statistics we want to track:

```java
public enum Rank {
    BEGINNER, CHAMPION, MASTER
}

public final class Statistics {
    public static final Key<Integer> ELO = new SimpleKey.Builder<>(Integer.class)
            .defaultValue(1200)
            .build();

    public static final Key<Integer> WIN_COUNT = new SimpleKey.Builder<>(Integer.class)
            .defaultValue(0)
            .build();

    public static final Key<Rank> RANK = new SimpleKey.Builder<>(Rank.class)
            .defaultValue(Rank.BEGINNER)
            .build();
}
```

Now, let's create an `onWin` method:

```java
public class Player {
    // ...

    public void onWin(int eloDelta) {
        // Update ELO
        int newElo = statistics.compute(Statistics.ELO, previous -> previous + eloDelta);
        broadcast("New player ELO is " + newElo);

        // Increase win count
        statistics.compute(Statistics.WIN_COUNT, IntegerValues::increment);
    }
}
```

Repositories have a really similar API to [`java.util.Map`](https://docs.oracle.com/javase/10/docs/api/java/util/Map.html). In this case, we used the `#compute(Key<T>, UnaryOperator<T>)` method to atomically update the ELO and win count of the player.
For the latter, we used the [`IntegerValues.increment`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/util/IntegerValues.html#increment(int)) method reference. Cellarium includes value utilities for most primitives
in the [`me.hugmanrique.cellarium.util`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/util/package-summary.html) package.

Finally, let's add a `levelUp` method to `Player`:

```java
public class Player {
    // ...

    public void levelUp() {
        // e.g. BEGINNER -> CHAMPION
        statistics.compute(Statistics.RANK, EnumValues::nextValue);
    }
}
```

Additional documentation for individual features can be found in the [javadocs](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/). For additional help, you can create an issue and I will try to respond as fast as I can.

# License

[MIT](LICENSE) &copy; [Hugo Manrique](https://hugmanrique.me)


[jitpack]: https://jitpack.io/v/hugmanrique/Cellarium.svg
[jitpack-url]: https://jitpack.io/#hugmanrique/Cellarium
[tests]: https://img.shields.io/travis/hugmanrique/Cellarium/master.svg
[tests-url]: https://travis-ci.org/hugmanrique/Cellarium
[license]: https://img.shields.io/github/license/hugmanrique/Cellarium.svg
[license-url]: LICENSE
