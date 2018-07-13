package com.yonyou.chaoke.base.esn.workmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * work调用链
 * 可组合复杂的调用链，但不允许网状调用链的存在
 */
public class WorkChain {

    /**
     * 调用链中最前面的work
     */
    public List<Work> mFirstWorks;

    /**
     * 调用链最后的work
     */
    public List<Work> mLastWorks;

    public List<Work> getFirstWork() {
        return mFirstWorks;
    }

    public List<Work> getLastWork() {
        return mLastWorks;
    }

    /**
     * 添加新的work
     *
     * 如果现有的为A => B 的任务链，添加works（包含任务C,D）后，则调用链为
     *                    ||=> C
     *          A => B => ||
     *                    || => D
     *
     * 如果现有的为
     *
     *               || => B
     *          A => ||
     *               || => C
     *
     *  则添加works(D, 只能为一个)后，任务链为
     *
     *               || => B => ||
     *          A => ||         || => D
     *               || => C => ||
     *
     * @param works
     */
    public WorkChain addWork(Work... works) throws TooMuchWorkHeadException {

        /**
         * 没有FirstWork，则将这次要放入的work全部当做第一个work
         */
        if (mFirstWorks == null) {
            mFirstWorks = new ArrayList<>();
            mLastWorks = new ArrayList<>();
            for (Work work : works) {
                mFirstWorks.add(work);
                mLastWorks.add(work);
            }
            return this;
        }

        if (mLastWorks != null && mLastWorks.size() > 1 && works.length > 1) {
            /**
             * 多对多的情况，无法处理，抛出异常
             */
            throw new TooMuchWorkHeadException();
        }

        if (mLastWorks.size() == 1) {
            /**
             *  之前的lastwork为1， 新添加的works可为1-n个
             */
            Work lastWork = mLastWorks.get(0);
            for (Work work : works) {
                lastWork.addNextWork(work);
                work.addPreWorks(mLastWorks);
            }

            mLastWorks = new ArrayList<>();
            for (Work work : works) {
                mLastWorks.add(work);
            }
        } else if (works.length == 1) {
            /**
             *  之前的lastWorks不是1个，但是works只有一个
             */
            Work currentWork = works[0];

            for (Work work : mLastWorks) {
                work.addNextWork(currentWork);
                currentWork.addPreWork(work);
            }

            mLastWorks = new ArrayList<>();
            mLastWorks.add(currentWork);
        }

        return this;
    }

    /**
     * 以WorkChain 的形式添加新的work
     * 原理同addWork
     * @param workChains
     */
    public WorkChain addWorkChain(WorkChain... workChains) throws TooMuchWorkHeadException {
        if (mFirstWorks == null) {
            /**
             * 当前任务链还没有任务
             */
            mFirstWorks = new ArrayList<>();
            mLastWorks = new ArrayList<>();

            for (WorkChain workChain : workChains) {
                mFirstWorks.addAll(workChain.getFirstWork());
                mLastWorks.addAll(workChain.getLastWork());
            }
        }


        if (mLastWorks != null && mLastWorks.size() > 1) {
            if ( workChains.length > 1) {
                /**
                 * 多对多的情况，无法处理，抛出异常
                 */
                throw new TooMuchWorkHeadException();
            }

            WorkChain workChain = workChains[0];
            if (workChain.getFirstWork() != null && workChain.getFirstWork().size() > 0) {
                /**
                 * 多对多的情况，无法处理，抛出异常
                 */
                throw new TooMuchWorkHeadException();
            }

        }

        if (mLastWorks.size() == 1) {
            /**
             * 当前的mLastWork为空
             */
            Work currentWork = mLastWorks.get(0);
            for (WorkChain workChain : workChains) {
                /**
                 * 将现有work和待添加workchain之间建立起前后指针关系
                 */
                for (Work work : workChain.getFirstWork()) {
                    currentWork.addNextWork(work);
                    work.addPreWork(currentWork);
                }

                /**
                 * 标记当前的调用链尾
                 */
                mLastWorks = new ArrayList<>();
                for (Work work : workChain.getLastWork()) {
                    mLastWorks.add(work);
                }
            }

        } else if (workChains.length == 1 && workChains[0].getFirstWork().size() == 1) {
            Work nextWork = workChains[0].getFirstWork().get(0);
            /**
             * 将现有work和待添加workchain之间建立起前后指针关系
             */
            for (Work work : mLastWorks) {
                work.addNextWork(nextWork);
                nextWork.addPreWork(nextWork);
            }

            mLastWorks = new ArrayList<>();
            mLastWorks.add(nextWork);
        }

        return this;

    }
}
