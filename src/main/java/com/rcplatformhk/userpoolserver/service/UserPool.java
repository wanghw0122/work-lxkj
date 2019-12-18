package com.rcplatformhk.userpoolserver.service;

public interface UserPool {
    public boolean exist(Object object);
    public boolean delete(Object object);
    public void save(Object o);

}
