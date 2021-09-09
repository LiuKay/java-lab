## 并发问题的根源

我们都知道 CPU，内存，I/O 是计算机里面的重要组成，但是在三者中一直存在一个问题从计算机的诞生到现在一直存在，那就是他们的速度差异。打个不太恰当的比喻，CPU 是天上一天，内存地上一年，I/O 是地上十年。为了提供计算机的性能，提高
CPU 的利用率，就有了下面的一些优化：

- CPU 增加缓存，平衡与内存的速度

- 操作系统增加进程，线程，分时复用 CPU，这样CPU看起来同时可以做多件事了

- 编译程序优化指令的执行顺序，更好的利用缓存

这样大大的提提高了计算机的能力，CPU的使用率，但是也引入了一些问题，这些问题就是并发编程里面3个根源问题：

#### 1 缓存 -- 可见性问题

单核CPU的情况下，所有线程都在一个 CPU上执行，使用的是同一个 CPU缓存，一个线程对缓存的写对另外一个线程是可见的。

但是在多核 CPU 的环境下，每个 CPU 都有自己的缓存，线程A 在 CPU-1 上执行，线程B 在 CPU-2
上执行，2个线程使用的缓存是不一样的，对同一个共享变量的修改，对另外一个线程是不可见。这个时候就会导致这个变量最终同步回内存的时候的值是不可预期的。

**可见性** - 一个线程对共享变量的修改，另外一个线程能够立刻看到。

#### 2 CPU 线程切换 -- 原子性问题

高级编程语言如 Java 的一条语句在 CPU 级别执行的时候可能是多条语句，而线程切换只能保证 CPU 指令的原子性，也就是说，线程切换可能发生在任何一条 CPU 指令的后面！

如下Java种的 `count++`, 实则对应 CPU 的3个操作，在这3个操作中可能发生线程切换，而且同时2个线程对变量的修改是互不可见的（看下面的可见性问题），这样最终导致在多线程情况下的数据不一致。

```java
static class VisibilityAndAtomic {
  private long count = 0;

  private void add100K() {
    int i = 0;
    while (i++ < 1000000) {
      /**
       * this line is not atomic.
       * suppose count is initiated 0,
       * At CPU level, there may be 3 steps, and thread switch can happen at any point.
       * 1. count loaded into PC register
       * 2. count +1
       * 3. write count into CPU cache or memory
       */
      count++;
    }
  }

  static void test() {
    VisibilityAndAtomic visibility = new VisibilityAndAtomic();
    Thread t1 = new Thread(() -> {
      visibility.add100K();
    }, "t1");

    Thread t2 = new Thread(() -> {
      visibility.add100K();
    }, "t2");

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(visibility.count); // count is less than 2000000
  }
}
```

#### 3 编译优化和指令重排序 -- 有序性问题

编译器为了优化性能，有时候会改变语句执行的先后顺序，如 “a=1;b=2” 优化成 “b=2;a=1” ，不影响最终的执行结果，但是在多线程的情况下，这种优化就有可能出现问题了。

在 Java 要实现一个单例对象，我们可能会这么写（双重检查），来看一下问题：

```java
static class Singleton {
  static Singleton instance;

  static Singleton getInstance() {
    if (instance == null) {
      synchronized (Singleton.class) {
        if (instance == null) {
          instance = new Singleton(); // What happens here???
        }
      }
    }
    return instance;
  }
}
```

当线程 A,B 同时进入 getInstance() 方法时，instance == null, 假设此时 线程 A 拿到锁，线程 B 等待，来看一下线程 A 执行到`instance = new Singleton()`
的时候会发生什么：

1. 分配一块内存 M
2. 在内存 M 上初始化 Singleton 对象
3. 然后 M 的地址赋值给 instance 变量

请注意，1,2,3 操作是可能发生指令重排的，假设执行路径是 1，3，2，

当线程 A 执行到 step3 时，发生了线程切换，此时线程 B 执行到 getInstance() 方法，就会发现`instance !=null` , 如果这个时候拿着这个 instance 去做操作的话就会发生空指针异常，因为此时的
instance 是没有实例化完的！

## Java 内存模型的解决方案

- volatile
- synchronized
- happens-before

## 原理

- monitor -- 管程模型
- 等待-唤醒 机制
- 死锁问题，破环死锁条件
- 各种锁的适用场景
- 异步编程

## 线程安全策略

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