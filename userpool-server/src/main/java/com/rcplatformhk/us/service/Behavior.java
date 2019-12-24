package com.rcplatformhk.us.service;


import com.rcplatformhk.us.task.Task;

public interface Behavior<V> {
    public Task g(V v);
}
