
## Java并发编程

参考资料：
1. Java 内存模型
- [JSR 133 (Java Memory Model) FAQ](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)
- [Java内存模型FAQ](http://ifeve.com/jmm-faq/)
- [JSR-133: JavaTMMemory Model and Thread Specification](https://www.cs.umd.edu/~pugh/java/memoryModel/jsr133.pdf)

### 1 基础知识

- CPU 多级缓存
- Java内存模型 JMM

### 2 线程安全性

- 原子性: AtomicXXX
- 可见性: volatile与synchronized区别
- 有序性: happens-before原则

### 3 对象发布

- 发布对象
- 对象逸出
- 单例模式举例说明线程安全性
  - 懒汉式
  - 饿汉式
  - 双重锁检验懒汉式
  - 枚举单例
  - 内部类单例
  - 单例注册表(Spring单例对象)

### 4 线程安全策略

- 不可变对象
  - JDK：Collections.unmodifiableXXX
  - Guava: ImmutableXXX
- 线程封闭
  - 堆栈封闭：局部变量
  - ThreadlLocal线程封闭：线程私有对象（SimpleDateFormat改造示例）
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

