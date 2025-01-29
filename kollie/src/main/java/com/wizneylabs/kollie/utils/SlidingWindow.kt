package com.wizneylabs.kollie.utils

class SlidingWindow<T>(val maxSize: Int) {
    val deque = ArrayDeque<T>(maxSize)

    fun add(item: T) {
        if (deque.size >= maxSize) {
            deque.removeFirst()
        }
        deque.addLast(item)
    }

    fun first(): T {
        return deque.first();
    }

    fun last(): T {
        return deque.last();
    }

    fun getItems(): List<T> = deque.toList()
}