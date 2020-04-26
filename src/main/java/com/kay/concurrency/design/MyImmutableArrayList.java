package com.kay.concurrency.design;

import java.util.*;
import java.util.function.Consumer;

/**
 * just a demo, not best practice
 */
public class MyImmutableArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{

    private final ArrayList<E> list;

    public static <E> MyImmutableArrayList<E> of(E... others ) {
        List<E> l = new ArrayList<>();
        if (others == null || others.length == 0) {
            throw new UnsupportedOperationException("length is 0");
        }
        Collections.addAll(l, others);
        return new MyImmutableArrayList<>(l);
    }

    public MyImmutableArrayList(Collection<? extends E> c) {
        list = new ArrayList<>(c);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    //TODO:
    @Override
    public Iterator<E> iterator() {
        return new UnmodifiedIterator<E>(list.iterator());
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray((T[]) a);
    }

    @Deprecated
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    @Deprecated
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    @Deprecated
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    @Deprecated
    public ListIterator<E> listIterator() {
        return new UnmodifiedListIterator<E>(list.listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new UnmodifiedListIterator<E>(list.listIterator(index));
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new MyImmutableArrayList<>(list.subList(fromIndex, toIndex));
    }

    private class UnmodifiedIterator<E> implements Iterator<E>{

        private final Iterator<E> iterator;

        public UnmodifiedIterator(Iterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override
        public void forEachRemaining(Consumer action) {
            iterator.forEachRemaining(action);
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public E next() {
            return iterator.next();
        }
    }

    private class UnmodifiedListIterator<E> implements ListIterator<E>{

        private final ListIterator<E> iterator;

        public UnmodifiedListIterator(ListIterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public E next() {
            return iterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public E previous() {
            return iterator.previous();
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public void set(Object o) {
            throw new UnsupportedOperationException("set");
        }

        @Override
        public void add(Object o) {
            throw new UnsupportedOperationException("add");
        }
    }
}
