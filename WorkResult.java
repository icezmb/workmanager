package com.alibaba.android.arouter.demo.workmanager;

/**
 * 任务执行完毕后的结果
 */
public class WorkResult<T> {

    /**
     * 执行成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = 1;

    /**
     * 结果状态码
     */
    private int mStatus = SUCCESS;

    private T mResult;

    public WorkResult(int status, T result) {
        mStatus = status;
        mResult = result;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public T getResult() {
        return mResult;
    }

    public void setmResult(T result) {
        this.mResult = result;
    }

}
