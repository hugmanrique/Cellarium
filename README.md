# :file_folder: Cellarium

[![jitpack][jitpack]][jitpack-url]
[![tests][tests]][tests-url]
[![license][license]][license-url]

**Cellarium** is a typesafe heterogeneous container API. Unlike an ordinary Java map, a `Repository` may contain `Item`s (i.e. keys) of different types.

This is a really powerful abstraction that can, for example, model database rows (that can have arbitrarily many columns of different types), user settings, or player statistics.

This project started as an exercise implementation of Joshua Bloch's [_Effective Java_, 3rd edition](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997) Item 33: Consider typesafe heterogeneous containers. In the process, I designed a more flexible and capable API and decided to release it.

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

You will need to have Java 11 or later (older versions _might_ work).

## Usage

As an example, we're going to create a statistics [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) that allows its clients to store and retrieve the value of any given statistic (in Cellarium's jargon, an [`Item`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Item.html)) for a given player.

First, let's create a [`Repository`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/Repository.html) field on the `Player` class:

```java
public class Player {
    private final Repository statistics;
    
    public Player() {
        // ...
        this.statistics = Repository.create();
    }
}
```

Now, let's create the chess statistics we want to manage:

```java
public final class ChessStatistics {
    public static final Item<Integer> ELO = new SimpleItem.Builder<>("elo", Integer.class)
            .defaultValue(1200)
            .build();
    
    public static final Item<Integer> WINS = new SimpleItem.Builder<>("wins", Integer.class)
            .defaultValue(0)
            .build();

    public static final Item<Boolean> WON_LAST_MATCH = new SimpleItem.Builder<>("won_last_match", Boolean.class)
            .defaultValue(false)
            .build();
} 
```

Finally, let's create an `onWin` method:

```java
public class Player {
    // ...
    
    public void onWin() {
        // Set new ranking
        int eloDiff = 4; // TODO Perform actual calculation
        statistics.apply(ChessStatistics.ELO, value -> value + eloDiff);

        // Increment win count by 1
        statistics.apply(ChessStatistics.WINS, IntStatistics::increment);

        // Set won last match
        statistics.setValue(ChessStatistics.WON_LAST_MATCH, true);
    }
}
```

Repositories can store any kind of object. In this case, we used [`IntStatistics`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/util/IntStatistics.html) to handle common tasks such as incrementing a statistic.

## Enum example

Let's add a `Rank` enum expressing the current position of a chess `Player`:

```java
public enum Rank {
    BEGINNER,
    CHAMPION,
    MASTER
}
```

Now, let's create the `RANK` statistic:

```java
public final class ChessStatistics {
    // ...
    
    public static final Item<Rank> RANK = new SimpleItem.Builder<>("rank", Rank.class)
            .defaultValue((Rank).BEGINNER)
            .build();
}
```

Finally, let's add a level-up method to the `Player` class:

```java
public class Player {
    // ...
    
    public void levelUp() {
        if (statistics.getValue(ChessStatistics.RANK).equals(Optional.of(Rank.MASTER))) {
            throw new IllegalStateException("Cannot rank-up player with highest rank");
        }
        
        statistics.apply(ChessStatistics.RANK, EnumStatistics::getNextValue);
    }
}
```

In this case we explicitly handled the case where the player already has the highest rank, but `EnumStatistics::getNextValue` cycles back to the first enum constant.

You can read [`EnumStatistics`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/util/EnumStatistics.html) for additional documentation.
Cellarium also includes utilities for booleans in [`BooleanStatistics`](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/me/hugmanrique/cellarium/util/BooleanStatistics.html).

Please note repositories are **not** thread-safe, so you will need to add synchronization for concurrent access.

Additional documentation for individual features can be found in the [Javadoc](https://jitpack.io/com/github/hugmanrique/Cellarium/master-SNAPSHOT/javadoc/). For additional help, you can create an issue and I will try to respond as fast as I can.  

# License

[MIT](LICENSE) &copy; [Hugo Manrique](https://hugmanrique.me)


[jitpack]: https://jitpack.io/v/hugmanrique/Cellarium.svg
[jitpack-url]: https://jitpack.io/#hugmanrique/Cellarium
[tests]: https://img.shields.io/travis/hugmanrique/Cellarium/master.svg
[tests-url]: https://travis-ci.org/hugmanrique/Cellarium
[license]: https://img.shields.io/github/license/hugmanrique/Cellarium.svg
[license-url]: LICENSE
