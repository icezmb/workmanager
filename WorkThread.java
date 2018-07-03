package com.alibaba.android.arouter.demo.workmanager;


public class WorkThread {

    /**
     * 线程的工作类型
     * 是执行在工作线程还是在UI线程
     */
    private WorkType mType;

    /**
     * 前一个work
     */
    private WorkThread mPrevWork;

    /**
     * 后一个work
     */
    private WorkThread mNextWork;

    /**
     * 延迟时间
     */
    private long mDelay;

    /**
     * 具体的执行任务
     */
    private IWorker mWork;

    public WorkThread(WorkType type, IWorker<?,?> work , long delay) {
        mType = type;
        mWork = work;
        mDelay = delay;
    }

    public WorkThread(WorkType type, IWorker<?,?> work) {
        mType = type;
        mWork = work;
    }

    public WorkType getType() {
        return mType;
    }

    public void setType(WorkType type) {
        this.mType = type;
    }

    public WorkThread getPrevWork() {
        return mPrevWork;
    }

    public void setPrevWork(WorkThread prevWork) {
        this.mPrevWork = prevWork;
    }

    public WorkThread getNextWork() {
        return mNextWork;
    }

    public void setNextWork(WorkThread nextWork) {
        this.mNextWork = nextWork;
    }

    public void setDelay(long delay) {
        mDelay = delay;
    }

    public long getDelay() {
        return mDelay;
    }


    public IWorker getWork() {
        return mWork;
    }
}
