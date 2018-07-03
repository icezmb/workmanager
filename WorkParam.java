package com.alibaba.android.arouter.demo.workmanager;

public class WorkParam<T> {

    public WorkThread workThread;

    public T param;

    public WorkParam(WorkThread workThread, T param) {
        this.workThread = workThread;
        this.param = param;
    }

}
