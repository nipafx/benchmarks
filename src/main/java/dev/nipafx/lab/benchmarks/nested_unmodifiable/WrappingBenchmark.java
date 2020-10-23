package dev.nipafx.lab.benchmarks.nested_unmodifiable;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class WrappingBenchmark extends NestedBenchmark {

	@Param({ "1", "10", "100", "1_000", "10_000" })
	private String depth = "1";

	private int _depth = 1;

	private Set<Integer> underlyingSet;

	@Setup(Level.Trial)
	public void setUp() {
		_depth = Integer.valueOf(depth.replace("_", ""));
		underlyingSet = new HashSet<>();
	}

	@Benchmark
	public Set<Integer> originalUnmodifiable() {
		Set<Integer> wrapped = underlyingSet;
		for (int i = 0; i < _depth; i++)
			wrapped = unmodifiableSet(wrapped);
		return wrapped;
	}

	@Benchmark
	public Set<Integer> withInstanceOfCheck() {
		Set<Integer> wrapped = underlyingSet;
		for (int i = 0; i < _depth; i++)
			wrapped = unmodifiableSetWithInstanceCheck(wrapped);
		return wrapped;
	}

	public static <T> Set<T> unmodifiableSet(Set<T> s) {
		return new UnmodifiableSet<>(s);
	}

	public static <T> Set<T> unmodifiableSetWithInstanceCheck(Set<T> s) {
		if (s instanceof UnmodifiableSet)
			return s;
		return new UnmodifiableSet<>(s);
	}

	/*
	 * The classes that `Collections.unmodifiable...` return are package visible.
	 * To test the exact same code, I copied them here without changes.
	 * IANAL, but I think the following copyright notice applies:
	 *
	 * ----
	 *
	 * Copyright (c) 1997, 2018, Oracle and/or its affiliates. All rights reserved.
	 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
	 *
	 * This code is free software; you can redistribute it and/or modify it
	 * under the terms of the GNU General Public License version 2 only, as
	 * published by the Free Software Foundation.  Oracle designates this
	 * particular file as subject to the "Classpath" exception as provided
	 * by Oracle in the LICENSE file that accompanied this code.
	 *
	 * This code is distributed in the hope that it will be useful, but WITHOUT
	 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
	 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
	 * version 2 for more details (a copy is included in the LICENSE file that
	 * accompanied this code).
	 *
	 * You should have received a copy of the GNU General Public License version
	 * 2 along with this work; if not, write to the Free Software Foundation,
	 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
	 *
	 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
	 * or visit www.oracle.com if you need additional information or have any
	 * questions.
	 *
	 * ----
	 *
	 */

	static class UnmodifiableCollection<E> implements Collection<E>, Serializable {

		private static final long serialVersionUID = 1820017752578914078L;

		final Collection<? extends E> c;

		UnmodifiableCollection(Collection<? extends E> c) {
			if (c == null)
				throw new NullPointerException();
			this.c = c;
		}

		public int size() {
			return c.size();
		}

		public boolean isEmpty() {
			return c.isEmpty();
		}

		public boolean contains(Object o) {
			return c.contains(o);
		}

		public Object[] toArray() {
			return c.toArray();
		}

		public <T> T[] toArray(T[] a) {
			return c.toArray(a);
		}

		public <T> T[] toArray(IntFunction<T[]> f) {
			return c.toArray(f);
		}

		public String toString() {
			return c.toString();
		}

		public Iterator<E> iterator() {
			return new Iterator<E>() {
				private final Iterator<? extends E> i = c.iterator();

				public boolean hasNext() {
					return i.hasNext();
				}

				public E next() {
					return i.next();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				public void forEachRemaining(Consumer<? super E> action) {
					// Use backing collection version
					i.forEachRemaining(action);
				}
			};
		}

		public boolean add(E e) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean containsAll(Collection<?> coll) {
			return c.containsAll(coll);
		}

		public boolean addAll(Collection<? extends E> coll) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection<?> coll) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> coll) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		// Override default methods in Collection
		@Override
		public void forEach(Consumer<? super E> action) {
			c.forEach(action);
		}

		@Override
		public boolean removeIf(Predicate<? super E> filter) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Spliterator<E> spliterator() {
			return (Spliterator<E>) c.spliterator();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Stream<E> stream() {
			return (Stream<E>) c.stream();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Stream<E> parallelStream() {
			return (Stream<E>) c.parallelStream();
		}

	}

	static class UnmodifiableSet<E> extends UnmodifiableCollection<E>
			implements Set<E>, Serializable {

		private static final long serialVersionUID = -9215047833775013803L;

		UnmodifiableSet(Set<? extends E> s) {
			super(s);
		}

		public boolean equals(Object o) {
			return o == this || c.equals(o);
		}

		public int hashCode() {
			return c.hashCode();
		}

	}

}
