package com.alibaba.android.arouter.demo.workmanager;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class WorkManager {

    /**
     * 第一个work
     */
    private WorkThread mFirstThread;

    /**
     * 当前的wrork
     */
    private WorkThread mCurrentThread;

    /**
     * 处理work
     */
    private final static int WORK_PROCESS = 1;

    private static WorkThreadPool threadPool = new WorkThreadPool("WorkManager", 3, 10);

    private static Handler mHandler = new MessageHandler(Looper.getMainLooper());

    private static class MessageHandler extends Handler {

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

            WorkParam tempWorkParam = null;
            if (msg.obj instanceof WorkParam) {
                tempWorkParam = (WorkParam) msg.obj;
            } else {
                return;
            }

            final WorkParam workParam = tempWorkParam;
            switch (msg.what) {
                case WORK_PROCESS:
                    if (workParam.workThread != null) {
                        if (workParam.workThread.getType() == WorkType.WORK_THREAD) {
                            threadPool.getThreadPoolExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    dealWork(workParam.workThread, workParam.param);
                                }
                            });
                        } else {
                            dealWork(workParam.workThread, workParam.param);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void dealWork(WorkThread workThread, Object param) {
            IWorker work = workThread.getWork();
            Object result = work.run(param);
            if (workThread.getNextWork() != null) {
                Message message = mHandler.obtainMessage();
                message.what = WORK_PROCESS;
                message.obj = new WorkParam<>(workThread.getNextWork(), result);
                mHandler.sendMessage(message);
            }
        }
    }

    private WorkManager() {

    }

    /**
     * 每次都新建一个WorkManager，以免导致执行混乱
     * @return WorkManager
     */
    public static WorkManager getInstance() {
        return new WorkManager();
    }

    /**
     * 执行任务
     * @param type 任务类型
     * @param work 具体工作
     */
    public WorkManager post(WorkType type, IWorker<?,?> work) {
        return post(type, work, 0);
    }

    /**
     * 执行任务，带延迟
     * @param type 任务类型
     * @param work 具体工作
     * @param delay 延迟时间
     */
    public WorkManager post(WorkType type, IWorker<?,?> work, long delay ) {
        WorkThread workThread = new WorkThread(type, work, delay);
        if (mFirstThread == null) {
            mFirstThread = workThread;
            mCurrentThread = workThread;
        } else {
            mCurrentThread.setNextWork(workThread);
            workThread.setPrevWork(mCurrentThread);
            mCurrentThread = workThread;
        }
        return this;
    }

    public void exec() {
        exec(null);
    }

    /**
     * 开始执行
     */
    public void exec(Object param) {
        if (mFirstThread == null) {
            return;
        }
        Message message = mHandler.obtainMessage();
        message.what = WORK_PROCESS;
        message.obj = new WorkParam<>(mFirstThread, param);
        mHandler.sendMessage(message);
    }

}
