# workmanager


  任务调度管理类
 
  version beta 0.1
 
 
  1.  一般的线性方式
 
   A => B => C
   WorkManager.getInstance().beginWith(A).then(B).then(c).enqueue();
 
 
   2. 带分支的调用
 
        || => B  => D => ||
   A => ||               || =>F
        || => C  => E => ||
 
    WorkChain wc1 = new WorkChain();
    wc1.addWork(B).addWork(D);
    WorkChain wc2 = new WorkChain();
    wc2.addWork(B).addWork(D);
    WorkManager.getInstance().beginWith(A).then(wc1, wc2).then(F).enqueue();
 
    3. 更复杂的分支
 
 
         || => B  => ||
         ||          || => E => ||
    A => || => C  => ||         ||
         ||                     || => G
         || => D  =======> F => ||
 
    WorkChain wc1 = new WorkChain();
    wc1.addWork(D).addWork(F);
    WorkChain wc2 = new WorkChain();
    wc2.addWork(B, C).addWork(E);
 
    WOrkManager.getInstance().beginWith(A).then(wc1, wc2).then(G).enqueue();
 
 
    4. 暂停或者退出Activity时，停止执行任务链
 
    WorkManager wm = WorkManager.getInstance();
    wm.addWork(A, B).then(D).then(E).enqueue();
 
    onDestroy or somewhere:
    wm.clear();
 
 
