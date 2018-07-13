package com.yonyou.chaoke.base.esn.workmanager;

/**
 * Work为空或者work中待执行内容为空时抛出此异常
 */
public class FirstWorkIsEmptyExistException extends Exception {

    public FirstWorkIsEmptyExistException() {
        super("work is null.");
    }

    public FirstWorkIsEmptyExistException(String msg) {
        super("work is null." + msg);
    }
}
