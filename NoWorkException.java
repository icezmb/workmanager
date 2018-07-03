package com.alibaba.android.arouter.demo.workmanager;

public class NoWorkException extends Exception {

    public NoWorkException(String message) {
        throw new RuntimeException("No work in pool, " + message);
    }
}
