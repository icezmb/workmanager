package com.yonyou.chaoke.base.esn.workmanager;

import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * 任务调度管理类
 *
 * version beta 0.1
 *
 * author 赵明斌
 *
 * 1.  一般的线性方式
 *
 *  A => B => C
 *  WorkManager.getInstance().beginWith(A).then(B).then(c).enqueue();
 *
 *
 *  2. 带分支的调用
 *
 *       || => B  => D => ||
 *  A => ||               || =>F
 *       || => C  => E => ||
 *
 *   WorkChain wc1 = new WorkChain();
 *   wc1.addWork(B).addWork(D);
 *   WorkChain wc2 = new WorkChain();
 *   wc2.addWork(B).addWork(D);
 *   WorkManager.getInstance().beginWith(A).then(wc1, wc2).then(F).enqueue();
 *
 *   3. 更复杂的分支
 *
 *
 *        || => B  => ||
 *        ||          || => E => ||
 *   A => || => C  => ||         ||
 *        ||                     || => G
 *        || => D  =======> F => ||
 *
 *   WorkChain wc1 = new WorkChain();
 *   wc1.addWork(D).addWork(F);
 *   WorkChain wc2 = new WorkChain();
 *   wc2.addWork(B, C).addWork(E);
 *
 *   WOrkManager.getInstance().beginWith(A).then(wc1, wc2).then(G).enqueue();
 *
 *
 *   4. 暂停或者退出Activity时，停止执行任务链
 *
 *   WorkManager wm = WorkManager.getInstance();
 *   wm.addWork(A, B).then(D).then(E).enqueue();
 *
 *   onDestroy or somewhere:
 *   wm.clear();
 *
 */
public class WorkManager {

    /**
     * work调用链
     */
    private WorkChain mWorkChain;


    /**
     * 处理任务调度的handler
     */
    private MessageHandler mHandler = new MessageHandler(Looper.getMainLooper());

    private WorkManager() {
    }

    /**
     * 每次都新建一个WorkManager，以免导致执行混乱
     * @return WorkManager
     */
    public static WorkManager getInstance() {
        return new WorkManager();
    }

    private WorkChain getWorkChain() {
        if (mWorkChain == null) {
            mWorkChain = new WorkChain();
        }
        return mWorkChain;
    }

    /**
     * 开始构建work调用链，work是单独开始的一个操作
     * @param type
     * @param runnable
     * @return
     * @throws FirstWorkAlreadyExistException
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager beginWith(WorkType type, ITask runnable) throws FirstWorkAlreadyExistException, FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        return beginWith(type, runnable, 0);
    }

    /**
     * 开始构建work调用链，work是单独开始的一个操作
     * @param type
     * @param runnable
     * @param delay
     * @return
     * @throws FirstWorkAlreadyExistException
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager beginWith(WorkType type, ITask runnable, long delay) throws FirstWorkAlreadyExistException, FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        if (getWorkChain().getFirstWork() != null && !getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkAlreadyExistException();
        }
        if (runnable == null) {
            throw new FirstWorkIsEmptyExistException();
        }

        Work work = new Work(type, runnable, delay);
        getWorkChain().addWork(work);
        return this;
    }

    /**
     * 开始构建work调用链，works是并发开始的一系列操作
     * @param works
     * @return
     * @throws FirstWorkAlreadyExistException
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager beginWith(Work... works)  throws FirstWorkAlreadyExistException, FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        if (getWorkChain().getFirstWork() != null && !getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkAlreadyExistException();
        }
        if (works == null || works.length == 0) {
            throw new FirstWorkIsEmptyExistException();
        }

        getWorkChain().addWork(works);
        return this;
    }

    /**
     * 开始构建work调用链， workChains是并发开始的一系列操作
     * @param workChains
     * @return
     * @throws FirstWorkAlreadyExistException
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager beginWith(WorkChain... workChains) throws FirstWorkAlreadyExistException, FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        if (getWorkChain().getFirstWork() != null && !getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkAlreadyExistException();
        }
        if (workChains == null || workChains.length == 0) {
            throw new FirstWorkIsEmptyExistException();
        }

        getWorkChain().addWorkChain(workChains);
        return this;
    }

    /**
     * 添加下一步的任务，是一个单独的work, 且不需要延时
     * @param type
     * @param runnable
     * @return
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager then(WorkType type, ITask runnable) throws FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        return then(type, runnable, 0);
    }

    /**
     * 添加下一步的work, 是单独的一个work
     * @param type
     * @param runnable
     * @param delay
     * @return
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager then(WorkType type, ITask runnable, long delay) throws FirstWorkIsEmptyExistException, TooMuchWorkHeadException {
        if (getWorkChain() == null || getWorkChain().getFirstWork() == null || getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkIsEmptyExistException();
        }

        Work work = new Work(type, runnable, delay);
        getWorkChain().addWork(work);
        return this;
    }

    /**
     * 添加下一步的work, works变长参数,可添加多个需要同步执行的任务
     * @param works
     * @return
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager then(Work... works)  throws FirstWorkIsEmptyExistException, TooMuchWorkHeadException  {
        if (getWorkChain() == null || getWorkChain().getFirstWork() == null || getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkIsEmptyExistException();
        }

        getWorkChain().addWork(works);
        return this;
    }

    /**
     * 添加下一步的work,可以通过workchain的形式添加
     * @param workChains
     * @return
     * @throws FirstWorkIsEmptyExistException
     * @throws TooMuchWorkHeadException
     */
    public WorkManager then(WorkChain... workChains)  throws FirstWorkIsEmptyExistException, TooMuchWorkHeadException  {
        if (getWorkChain() == null || getWorkChain().getFirstWork() == null || getWorkChain().getFirstWork().isEmpty()) {
            throw new FirstWorkIsEmptyExistException();
        }

        getWorkChain().addWorkChain(workChains);
        return this;
    }

    /**
     * 队列中的任务，开始执行
     */
    public void enqueue(WorkData data) {
        WorkChain workChain = getWorkChain();
        if (workChain == null || workChain.getFirstWork() == null || workChain.getFirstWork().isEmpty()) {
            return;
        }

        List<Work> firstWorks = workChain.getFirstWork();
        for (Work work : firstWorks) {
            Message message = mHandler.obtainMessage();
            message.what = MessageHandler.WORK_PROCESS;
            work.setInData(data);
            message.obj = work;
            if (work.getDelay() == 0) {
                mHandler.sendMessage(message);
            } else {
                mHandler.sendMessageDelayed(message, work.getDelay());
            }
        }
    }


    /**
     * 将workChain中还没有执行的任务停止
     */
    public void clear() {
        if (mHandler != null) {
            mHandler.stop();
        }
    }

}
