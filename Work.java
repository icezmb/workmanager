package com.yonyou.chaoke.base.esn.workmanager;


import java.util.ArrayList;
import java.util.List;

/**
 * 执行任务的基本单位
 */
public class Work {

    /**
     * 线程的工作类型
     * 是执行在工作线程还是在UI线程
     */
    private WorkType mType;

    /**
     * 与其相连的前置works
     */
    private List<Work> mPrevWork;

    /**
     * 与其相连的后置works
     */
    private List<Work> mNextWork;

    /**
     * 延迟时间
     */
    private long mDelay;

    /**
     * 具体的执行任务
     */
    private ITask mWork;

    /**
     * 是否执行完毕
     */
    private boolean mWorkDone = false;

    /**
     * 传入的参数
     */
    private WorkData mInData = new WorkData();

    /**
     * 传出的参数
     */
    private WorkData mOutData = new WorkData();

    private WorkStatus mStatus;

    public Work(WorkType type, ITask work , long delay) {
        mType = type;
        mWork = work;
        mDelay = delay;
    }

    public Work(WorkType type, ITask work) {
        mType = type;
        mWork = work;
    }

    public WorkType getType() {
        return mType;
    }

    public void setType(WorkType type) {
        this.mType = type;
    }

    public List<Work> getPrevWorks() {
        return mPrevWork;
    }

    public void addPreWorks(List<Work> prevWork) {
        if (mPrevWork == null) {
            mPrevWork = new ArrayList<>();
        }
        mPrevWork.addAll(prevWork);
    }

    public List<Work> getNextWork() {
        return mNextWork;
    }

    public void addPreWork(Work work) {
        if (mPrevWork == null) {
            mPrevWork = new ArrayList<>();
        }
        mPrevWork.add(work);
    }

    public void addNextWork(Work work) {
        if (mNextWork == null) {
            mNextWork = new ArrayList<>();
        }
        mNextWork.add(work);
    }

    public void addNextWorks(List<Work> nextWork) {
        if (mPrevWork == null) {
            mPrevWork = new ArrayList<>();
        }
        mNextWork.addAll(nextWork);
    }

    public void setDelay(long delay) {
        mDelay = delay;
    }

    public long getDelay() {
        return mDelay;
    }


    public ITask getWork() {
        return mWork;
    }

    public boolean workDone() {
        return mWorkDone;
    }

    public void setWorkDone(boolean workDone) {
        mWorkDone = workDone;
    }

    public void setInData(WorkData data) {
        mInData = data;
    }

    public WorkData getInData() {
        return mInData;
    }

    public void setOutData(WorkData data) {
        mOutData = data;
    }

    public WorkData getOutData() {
        return mOutData;
    }

    public void setStatus(WorkStatus status) {
        mStatus = status;
    }

    public WorkStatus getStatus() {
        return mStatus;
    }
}
