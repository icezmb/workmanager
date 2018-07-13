package com.yonyou.chaoke.base.esn.workmanager;


/**
 * work如果现有调用链是A  =>  B
 *                  ||  =>  C
 * 待加入调用链末端的任务不止一个，则会发生该Exception, 因为多对多的调用链会是执行发生混乱。
 *
 *
 */
public class TooMuchWorkHeadException extends Exception {

    public TooMuchWorkHeadException() {
        super("too much work head exception");
    }
}
