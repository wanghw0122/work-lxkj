package com.rcplatformhk.common;

public class MapToObjectException extends RuntimeException {
    public MapToObjectException(){
        super();
    }

    public MapToObjectException(String message){
        super(message);
    }

    public MapToObjectException(String message, Throwable cause){
        super(message,cause);
    }

    public MapToObjectException(Throwable cause) {
        super(cause);
    }
}
