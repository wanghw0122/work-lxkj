package com.rcplatformhk.userpoolserver.service;

import com.rcplatformhk.userpoolserver.task.Task;

public interface Behavior<V> {
    public Task g(V v);
}
