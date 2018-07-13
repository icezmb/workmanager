package com.yonyou.chaoke.base.esn.workmanager;

/**
 * Worker接口
 */
public interface ITask {

    /**
     * 执行具体方法接口
     * @return T 返回结果
     */
    WorkData run(WorkData data);
}
