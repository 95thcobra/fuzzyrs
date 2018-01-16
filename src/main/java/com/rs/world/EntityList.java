package com.rs.world;

import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityList<T extends Entity> extends AbstractCollection<T> {
	private static final int MIN_VALUE = 1;
	public Object[] entities;
	public Set<Integer> indicies = new HashSet<Integer>();
	public int curIndex = MIN_VALUE;
	public int capacity;
	private final Object lock = new Object();

	public EntityList(final int capacity) {
		entities = new Object[capacity];
		this.capacity = capacity;
	}

	@Override
	public boolean add(final T entity) {
		synchronized (lock) {
			add(entity, curIndex);
			return true;
		}
	}

	public void remove(final T entity) {
		synchronized (lock) {
			entities[entity.getIndex()] = null;
			indicies.remove(entity.getIndex());
			decreaseIndex();
		}
	}

	@SuppressWarnings("unchecked")
	public T remove(final int index) {
		synchronized (lock) {
			final Object temp = entities[index];
			entities[index] = null;
			indicies.remove(index);
			decreaseIndex();
			return (T) temp;
		}
	}

	@SuppressWarnings("unchecked")
	public T get(final int index) {
		synchronized (lock) {
			if (index >= entities.length)
				return null;
			return (T) entities[index];
		}
	}

	public void add(final T entity, final int index) {
		if (entities[curIndex] != null) {
			increaseIndex();
			add(entity, curIndex);
		} else {
			entities[curIndex] = entity;
			entity.setIndex(index);
			indicies.add(curIndex);
			increaseIndex();
		}
	}

	@Override
	public Iterator<T> iterator() {
		synchronized (lock) {
			return new EntityListIterator<T>(entities, indicies, this);
		}
	}

	public void increaseIndex() {
		curIndex++;
		if (curIndex >= capacity) {
			curIndex = MIN_VALUE;
		}
	}

	public void decreaseIndex() {
		curIndex--;
		if (curIndex <= capacity) {
			curIndex = MIN_VALUE;
		}
	}

	public boolean contains(final T entity) {
		return indexOf(entity) > -1;
	}

	public int indexOf(final T entity) {
		for (final int index : indicies) {
			if (entities[index].equals(entity))
				return index;
		}
		return -1;
	}

	@Override
	public int size() {
		return indicies.size();
	}
}
