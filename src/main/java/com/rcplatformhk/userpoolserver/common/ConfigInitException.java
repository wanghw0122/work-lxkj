package com.rcplatformhk.userpoolserver.common;

public class ConfigInitException extends RuntimeException {

    public ConfigInitException(){
        super();
    }

    public ConfigInitException(String message){
        super(message);
    }

    public ConfigInitException(String message, Throwable cause){
        super(message,cause);
    }

    public ConfigInitException(Throwable cause) {
        super(cause);
    }
}
