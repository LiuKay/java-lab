
#### 并发问题的根源
- 缓存 -- 可见性问题
- 编译优化和指令重排序 -- 有序性问题
- CPU 线程切换 -- 原子性问题

#### Java 内存模型的解决方案
- volatile
- synchronized 
- happens-before

### 原理
- monitor -- 管程模型
- 等待-唤醒 机制
- 死锁问题，破环死锁条件
- 各种锁的适用场景
- 异步编程

### 线程安全策略

- 不可变对象
  - JDK：Collections.unmodifiableXXX
  - Guava: ImmutableXXX
- 线程封闭
  - 堆栈封闭：局部变量
  - ThreadlLocal线程封闭：线程私有对象
- 同步容器
    - ArrayList->Vector
    - HashMap->HashTable
    - Collections.synchronizedXXX
- 并发容器
    - ArrayList->CopyOnWriteArrayList
    - HashSet->CopyOnWriteArraySet
    - TreeSet->ConcurrentSkipListSet
    - HashMap->ConcurrentHashMap
    - TreeMap->ConcurrentSkipListMap
- 异步编程工具
    - FutureTask
    - CompletableFuture\CompletableService
    - ForkJoinTask 