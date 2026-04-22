package com.packt.cookbook.libraries.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Utility class for hold a pair of objects
 *
 * @param <T> - type of first object
 * @param <V> - type of second object
 */
public class Pair<T, V> implements Serializable {

	private static final long serialVersionUID = 840789606404857451L;

	/** First object */
	protected T first;
	/** Second object */
	protected V second;

	/**
	 * Default constructor
	 */
	public Pair() {

	}

	/**
	 * Constructor with parameters
	 *
	 * @param first  - value of first object
	 * @param second - value of second object
	 */
	public Pair(T first, V second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Note: two pairs with empty values (first == null and second == null)
	 * are equals.
	 *
	 * @see Object#equals(Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj instanceof Pair<?, ?>)) {
			Pair<?, ?> other = (Pair<?, ?>) obj;

			return (Objects.equals(first, other.first)) &&
					(Objects.equals(second, other.second));
		}

		return false;
	}

	/**
	 * @return the first
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * @return the second
	 */
	public V getSecond() {
		return second;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/**
	 * Set first param
	 *
	 * @param first first param
	 * @return this pair
	 */
	public Pair<T, V> setFirst(T first) {
		this.first = first;
		return this;
	}

	/**
	 * Set second param
	 *
	 * @param second second param
	 * @return this pair
	 */
	public Pair<T, V> setSecond(V second) {
		this.second = second;
		return this;
	}

	/**
	 * Set both params of pair
	 *
	 * @param first  first param
	 * @param second second param
	 * @return this pair
	 */
	public Pair<T, V> setBoth(T first, V second) {
		this.first = first;
		this.second = second;
		return this;
	}


	@Override
	public String toString() {
		return  getClass().getSimpleName() + "{first=" + first + ", second=" + second + '}';
	}
}
