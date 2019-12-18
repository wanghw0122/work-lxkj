package com.rcplatformhk.userpoolserver.sink;

public interface Sink<T> {
    void sink(T t);
}
