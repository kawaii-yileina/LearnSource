package com.elaina.MySpring.collections;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * ClassName: MyLinkedList
 * Package: com.elaina.MySpring.collections
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/16 22:29
 */
public class MyLinkedList<E> implements List<E>, Serializable {

    private transient Node<E> first;

    private transient Node<E> last;

    private transient int size = 0;

    private transient int modCount;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) {
            result[i] = e;
            i++;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int i = 0;
        for (E e : this) {
            a[i] = (T) e;
            i++;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        if (first == null) {
            first = new Node<>(null, last, e);
            last = new Node<>(first, null, e);
            first.next = last;
        } else if (size == 1) {
            last.value = e;
        } else {
            last = new Node<>(last, null, e);
            last.pre.next = last;
        }
        size++;
        modCount++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> node = findNodeByElement(o);
        if (node == null) {
            return false;
        }
        unlinkNode(node);
        return true;
    }

    public boolean remove(Object o, boolean isRemoveAll) {
        if (isRemoveAll) {
            List<Node<E>> nodeList = findNodesByElement(o);
            if (nodeList.isEmpty()) {
                return false;
            }
            nodeList.forEach(this::unlinkNode);
            return true;
        }
        Node<E> node = findNodeByElement(o);
        if (node == null) {
            return false;
        }
        unlinkNode(node);
        return true;
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        Object[] array = c.toArray();
        for (Object o : array) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] array = c.toArray();
        for (Object o : array) {
            add((E) o);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Object[] array = c.toArray();
        for (Object o : array) {
            add(index, (E) o);
            index++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Object[] array = c.toArray();
        for (Object o : array) {
            remove(o, true);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<Object> removeList = new ArrayList<>();
        forEach(e -> {
            if (!c.contains(e)) {
                removeList.add(e);
            }
        });
        removeAll(removeList);
        return !removeList.isEmpty();
    }

    @Override
    public void clear() {
        int t = size;
        for (int i = 0; i < t; i++) {
            unlinkFirst();
        }
    }

    protected void unlinkNode(Node<E> node) {
        if (first == node) {
            unlinkFirst();
            return;
        } else if (last == node) {
            unlinkLast();
            return;
        }
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.pre = null;
        node.next = null;
        node.value = null;
        size--;
        modCount++;
    }

    protected void unlinkFirst() {
        if (size == 1) {
            first = null;
            last = null;
            size--;
            return;
        }
        Node<E> t = first;
        first = first.next;
        first.pre =null;
        t.next = null;
        size--;
        modCount++;
    }

    protected void unlinkLast() {
        if (size == 1) {
            first = null;
            last = null;
            size--;
            return;
        }
        Node<E> t = last;
        last = last.pre;
        last.next = null;
        t.pre = null;
        size--;
        modCount++;
    }

    protected void checkEmpty() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("链表里还没有元素");
        }
    }

    @Override
    public E get(int index) {
        return findNodeByIndex(index).value;
    }

    private Node<E> findNodeByIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException("超出最大索引");
        }
        index = indexTransfer(index);
        if (index == 0) {
            return first;
        } else if (index == size  - 1) {
            return last;
        }
        if (index < (size - 1) / 2) {
            return findNodeFromFirst(index);
        }
        return findNodeFromLast(index);

    }

    private Node<E> findNodeByElement(Object o) {
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(node.value, o)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }


    private List<Node<E>> findNodesByElement(Object o) {
        Node<E> node = first;
        List<Node<E>> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (Objects.equals(node.value, o)) {
                result.add(node);
            }
            node = node.next;
        }
        return result;
    }

    private int indexTransfer(int index) {
        if (index < 0) {
            return size + index;
        }
        return index;
    }


    protected Node<E> findNodeFromFirst(int index) {
        checkEmpty();
        Node<E> node = first.next;
        for (int i = 1; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    protected Node<E> findNodeFromLast(int index) {
        checkEmpty();
        Node<E> node = last.pre;
        for (int i = size - 2; i > index; i--) {
            node = node.pre;
        }
        return node;
    }

    private boolean isElementIndex(int index) {
        return index < size && index >= -size;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = findNodeByIndex(index);
        E oldValue = node.value;
        node.value = element;

        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        index = indexTransfer(index);
        if (index == 0) {
            addFirst(element);
            return;
        } else if (index == size) {
            addLast(element);
            return;
        }
        Node<E> node = findNodeByIndex(index);
        Node<E> newNode = new Node<>(node.pre, node, element);
        node.pre.next = newNode;
        node.pre = newNode;
        size++;
        modCount++;
    }

    private void addFirst(E element) {
        Node<E> node = new Node<>(null, first, element);
        first.pre = node;
        first = node;
        size++;
        modCount++;
    }

    private void addLast(E element) {
        add(element);
    }

    @Override
    public E remove(int index) {
        Node<E> node = findNodeByIndex(index);
        E oldValue = node.value;
        unlinkNode(node);
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(node.value, o)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> node = last;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(node.value, o)) {
                return size - 1 - i;
            }
            node = node.pre;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        MyLinkedList<Object> removeList = new MyLinkedList<>();
        forEach(e ->{
            if (filter.test(e)) {
                removeList.add(e);
            }
        });
        removeAll(removeList);
        return !removeList.isEmpty();
    }

    @Override
    public Stream<E> stream() {
        return List.super.stream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        List.super.forEach(action);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        MyLinkedList<E> list = new MyLinkedList<>();
        checkRange(fromIndex, toIndex);
        fromIndex = indexTransfer(fromIndex);
        toIndex = indexTransfer(toIndex);
        Node<E> node = findNodeByIndex(fromIndex);
        for (int i = 0; i < toIndex - fromIndex; i++) {
            list.addLast(node.value);
            node = node.next;
        }
        return list;
    }


    private void checkRange(int fromIndex, int toIndex) {
        fromIndex = indexTransfer(fromIndex);
        toIndex = indexTransfer(toIndex);
        if (fromIndex < 0 || toIndex > size || fromIndex >= toIndex) {
            throw new IndexOutOfBoundsException();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0;i < size;i++) {
            sb.append(findNodeByIndex(i).value);
            if (i != size - 1) {
                sb.append(",");
            }
        }
        return sb.append("]").toString();
    }

    @SuppressWarnings("all")
    protected class Node<E> {
        public Node(Node<E> pre, Node<E> next, E value) {
            this.pre = pre;
            this.next = next;
            this.value = value;
        }

        public Node() {

        }

        Node<E> pre;
        Node<E> next;
        E value;
    }

    class MyIterator implements Iterator<E> {

        private Node<E> cursor = first;

        private final int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            Node<E> result = cursor;
            cursor = cursor.next;
            if (first.next == last) {
                cursor = null;
            }
            return result.value;
        }
    }
}
