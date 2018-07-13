package com.yonyou.chaoke.base.esn.workmanager;

/**
 * 已经存在第一个work的情况下，继续使用beginWith，会抛出此Exception
 */
public class FirstWorkAlreadyExistException extends Exception {

    public FirstWorkAlreadyExistException() {
        super("first work already exist, should use one beginWith method in one workManager.");
    }

    public FirstWorkAlreadyExistException(String msg) {
        super("first work already exist, should use one beginWith method in  one workManager." + msg);
    }
}
