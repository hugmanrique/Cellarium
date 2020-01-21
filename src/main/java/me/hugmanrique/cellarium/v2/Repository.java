package me.hugmanrique.cellarium.v2;

/**
 * A typesafe heterogeneous container that maps {@link Key}s to values.
 *
 * <p>Unlike a regular map, the {@link Key} is parameterized instead of the map.
 * This means that all the keys are of different types, allowing a {@code Repository}
 * instance to hold values of many (i.e. heterogeneous) types. This type token is
 * used to guarantee that the type of a value agrees with its {@link Key}.
 *
 * @since 1.0
 * @author Hugo Manrique
 * @see <a href="http://www.informit.com/articles/article.aspx?p=2861454&seqNum=8">Joshua Bloch's Effective Java Item 33</a>
 */
public interface Repository {

    boolean contains(Key<?> item);
}
