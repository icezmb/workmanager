package com.yonyou.chaoke.base.esn.workmanager;

/**
 * work运行的状态
 */
public enum WorkStatus {

    /**
     * 完成初始化
     */
    INIT(0),

    /**
     * 准备好，可以运行
     */
    READY(1),

    /**
     * 正在执行状态
     */
    RUNNING(2),

    /**
     * 暂停
     */
    PAUSE(3),

    /**
     * 发生错误
     */
    ERROR(4),

    /**
     * 已停止
     */
    STOP(5),

    /**
     * 运行结束
     */
    FINISH(6);

    private final int value;

    WorkStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
