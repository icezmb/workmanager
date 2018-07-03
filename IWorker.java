package com.alibaba.android.arouter.demo.workmanager;

/**
 * Worker接口
 */
public interface IWorker<T, D> {

    /**
     * 执行具体方法接口
     * @return T 返回结果
     */
    T run(D param);
}
