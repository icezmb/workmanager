package com.yonyou.chaoke.base.esn.workmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.List;

/**
 *
 * 处理任务链的调度handler
 *
 * version beta 0.1
 *
 * author 赵明斌
 */
public class MessageHandler extends Handler {

    public final String TAG = MessageHandler.class.getSimpleName();

    private static WorkThreadPool threadPool = new WorkThreadPool("WorkManager", 3, 10);

    /**
     * 临时停止的标志
     */
    private volatile boolean mStop = false;

    /**
     * 处理work
     */
    public final static int WORK_PROCESS = 1;

    /**
     * work的调度
     */
    public final static int WORK_SCHEDULE = 2;


    public MessageHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg == null) {
            return;
        }
        if (msg.obj == null) {
            return;
        }

        Work work = null;
        if (msg.obj instanceof Work) {
            work = (Work) msg.obj;
        } else {
            return;
        }

        final Work finalWork = work;
        switch (msg.what) {
            case WORK_PROCESS:
                work.setStatus(WorkStatus.RUNNING);
                if (finalWork.getType() == WorkType.WORK_THREAD) {
                    threadPool.getThreadPoolExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            dealWork(finalWork);
                        }
                    });
                } else {
                    dealWork(finalWork);
                }
                break;
            case WORK_SCHEDULE:
                scheduleWork(finalWork);
                break;
            default:
                break;
        }
    }

    /**
     * 调度work
     * @param work
     */
    private void scheduleWork(Work work) {
        /**
         * 终止状态下停止停止
         */
        if (mStop) {
            Log.d(TAG, "interrupt work chain.");
            return;
        }

        /**
         * 结束状态
         */
        work.setStatus(WorkStatus.FINISH);
        /**
         * 判断当前work的nextwork有哪些可以运行
         */
        List<Work> nextWorks = work.getNextWork();
        if (nextWorks != null && !nextWorks.isEmpty()) {
            for (Work workItem : nextWorks) {
                List<Work> preWorks = workItem.getPrevWorks();
                boolean allFinished = true;
                for (Work preWork : preWorks) {
                    /**
                     * 所有的prework都处于运行结束状态才可以运行此work
                     */
                    if (preWork.getStatus() != WorkStatus.FINISH) {
                        allFinished = false;
                        break;
                    }
                }

                if (allFinished) {
                    WorkData inData = workItem.getInData();
                    for (Work preWork : preWorks) {
                        /**
                         * 将所有的prework的outdata，设为当前work的indata
                         */
                        inData.putAll(preWork.getOutData());
                    }
                    Message message = obtainMessage();
                    message.what = WORK_PROCESS;
                    message.obj = workItem;
                    if (workItem.getDelay() == 0) {
                        sendMessage(message);
                    } else {
                        sendMessageDelayed(message, workItem.getDelay());
                    }
                }

            }
        }
    }

    /**
     * 执行work
     * @param work
     */
    private void dealWork(Work work) {
        if (mStop) {
            Log.d(TAG, "interrupt work chain.");
            return;
        }

        ITask task = work.getWork();
        /**
         * 真正执行work的具体内容
         */
        WorkData outData = task.run(work.getInData());
        work.setOutData(outData);
        Message message = obtainMessage();
        message.what = WORK_SCHEDULE;
        message.obj = work;
        sendMessage(message);

    }

    /**
     * 结束剩余任务链的执行
     */
    public void stop() {
        mStop = true;
    }
}

