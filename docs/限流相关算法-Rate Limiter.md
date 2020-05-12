### 限流相关算法



#### 1 Token bucket
令牌桶算法的详细描述如下：

1. 令牌以固定的速率添加到令牌桶中，假设限流的速率是 r/秒，则令牌添加到桶中的速率是 1/r。
2. 假设令牌桶的容量是 b, 如果令牌桶已满，则新的令牌会被丢弃。
3. 请求能够通过限流器的前提是令牌桶中有令牌。

Demo: [RateLmiterDemo](https://github.com/LiuKay/JavaProfessional/blob/master/src/main/java/com/kay/concurrency/design/RateLimiterDemo.java)

#### 2 Leaky bucket

#### 3 Fixed window counter

#### 4 Sliding logs

#### 5 Sliding window counter 