package me.hugmanrique.cellarium;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A typesafe heterogeneous container that maps {@link Key}s to values.
 *
 * <p>Unlike a regular map, the {@link Key} is parameterized instead of the map.
 * This means that all the keys are of different types, allowing a {@code Repository}
 * instance to hold values of many (i.e. heterogeneous) types. This type token is
 * used to guarantee that the type of a value agrees with its {@link Key}.
 *
 * <p>{@code null} {@link Key}s are prohibited. Attempting to insert a {@code null}
 * key will throw {@link NullPointerException}.
 *
 * @since 1.0
 * @author Hugo Manrique
 * @see <a href="http://www.informit.com/articles/article.aspx?p=2861454&seqNum=8">Joshua Bloch's Effective Java Item 33</a>
 */
public interface Repository {

    /**
     * Returns the value to which the specified key is mapped, or the key's
     * default value if this repository contains no mapping for the key.
     *
     * @param key key whose associated value is to be returned
     * @param <T> the type of the value
     * @return the value to which the specified key is mapped, or the key's default
     *         value if this repository contains no mapping for the key
     */
    @Nullable
    <T> T get(Key<T> key);

    /**
     * Associates the specified value with the specified key in this repository.
     * If the key already had an associated value, the old value is replaced by
     * the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param <T> the type of the value
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}
     */
    @Nullable
    <T> T put(Key<T> key, T value);

    /**
     * If the specified key is not associated with a value, associates it with the
     * given value and returns {@code null}, else returns the current value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param <T> the type of the value
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}
     */
    @Nullable
    <T> T putIfAbsent(Key<T> key, T value);

    /**
     * Attempts to compute a mapping for the specified key and its current mapped
     * value (or the key's default value if there is no current mapping).
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * @param key key with which the computed value is to be associated
     * @param remappingFunction remapping function to compute a value
     * @param <T> the type of the value
     * @return the new value associated with the specified key, or {@code null} if none
     */
    @Nullable
    <T> T compute(Key<T> key, UnaryOperator<T> remappingFunction);

    /**
     * If the specified key is not already associated with a value, attempts to
     * compute its value using the given mapping function and enters it into
     * this repository.
     *
     * <p>Unlike regular maps, if the mapping function returns {@code null},
     * a {@link NullPointerException} is thrown.
     *
     * @param key key with which the computed value is to be associated
     * @param mappingFunction mapping function to compute a value
     * @param <T> the type of the value
     * @return the current (existing or computed) value associated with the specified key
     * @throws NullPointerException if the mapping function returns {@code null}
     */
    <T> T computeIfAbsent(Key<T> key, Supplier<? extends T> mappingFunction);

    /**
     * If the value associated to the specified key is present, attempts to compute
     * a new mapping given its current mapped value.
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * @param key key with which the computed value is to be associated
     * @param remappingFunction remapping function to compute a value
     * @param <T> the type of the value
     * @return the new value associated with the specified key, or {@code null} if none
     */
    @Nullable
    <T> T computeIfPresent(Key<T> key, UnaryOperator<T> remappingFunction);

    /**
     * Replaces the value associated with the specified key only if it is
     * currently mapped to some value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param <T> the type of the value
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}
     */
    @Nullable
    <T> T replace(Key<T> key, T value);

    /**
     * Replaces the value associated with the specified key only if it is
     * currently mapped to {@code oldValue}.
     *
     * @param key key with which the specified value is to be associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @param <T> the type of the value
     * @return {@code true} if the value was replaced
     */
    <T> boolean replace(Key<T> key, T oldValue, T newValue);

    /**
     * Removes the mapping for a key from this repository if it is present.
     *
     * @param key key whose mapping is to be removed from the repository
     * @param <T> the type of the value
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}
     */
    @Nullable
    <T> T remove(Key<T> key);

    /**
     * Removes the mapping for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key key whose mapping is to be removed from the repository
     * @param value value expected to be associated with the specified key
     * @param <T> the type of the value
     * @return {@code true} if the value was removed
     */
    <T> boolean remove(Key<T> key, T value);

    /**
     * Removes all of the mappings from this repository.
     */
    void clear();

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this repository is to be tested
     * @return {@code true} if this repository contains a mapping for the specified key
     */
    boolean contains(Key<?> key);

    /**
     * Returns the number of key-value mappings in this repository.
     *
     * @return the number of key-value mappings in this repository
     */
    int size();

    /**
     * Returns {@code true} if this repository contains no key-value mappings.
     *
     * @return {@code true} if this repository contains no key-value mappings
     */
    boolean isEmpty();
}
