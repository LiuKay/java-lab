## Java 异常处理最佳实践

### 1 永远不要在 catch 代码块中忽略异常

这条原则放在第一位。不要忽略你遇到的异常，要么将其抛出，要么记录日志，如果确实是可以不做处理的异常，也请将原因写`catch`
块的注释里面。因为如果忽略了异常，你将永远不知道异常的原因，将来也无法避免它。

1.1 记录日志

```java
catch(NoSuchMethodException ex){
    log.error(ex);
}
```

1.2 重新包装抛出异常

```java
catch(NoSuchMethodException ex){
    throw new BusinessExceoption("business logic faild.",ex);
}
```

1.3 为不处理的异常补充详细说明

```java
catch(NoSuchMethodException ignored){
    //此处异常不做处理，原因.....
}
```

### 2 在`Finally`代码块中清理资源，或使用`try-with-resource`语法

常见问题：try 最后的语句可能永远都不会执行

```java
public void doNotCloseResourceInTry(){
		FileInputStream inputStream;
		try{
		File file=new File("./tmp.txt");
		inputStream=new FileInputStream(file);

		// 使用 inputStream

		// 不要这么做：如果在上面处理流的过程中发生异常，这里的代码永远都不会被执行
		inputStream.close();
		}catch(IOException e){
		log.error(e);
		}
		}
```

2.1 在`finally`里面清理资源

```java
public void closeResourceInFinally(){
		FileInputStream inputStream=null;
		try{
		File file=new File("./tmp.txt");
		inputStream=new FileInputStream(file);
		// use the inputStream to read a file
		}catch(FileNotFoundException e){
		log.error(e);
		}finally{
		if(inputStream!=null){
		try{
		inputStream.close();
		}catch(IOException e){
		log.error(e);
		}
		}
		}
		}
```

2.2 使用`try-with-resource`语法自动关闭资源

```java
public void automaticallyCloseResource(){
		File file=new File("./tmp.txt");
		try(FileInputStream inputStream=new FileInputStream(file)){
		// use the inputStream to read a file
		}catch(IOException e){
		log.error(e);
		}
		}
```

### 3 如果要抛出异常，优先使用具体的异常类型

```java
public void doNotDoThis()throws Exception{
		//...
		}

public void doThis()throws NumberFormatException{
		//...
		}
```

### 4 使用`@throws`为你抛出的异常写下注释

请记住，你所抛出的异常也是你暴露出的接口的一部分，应该在你的文档中尽量说明在什么情况下会抛出什么异常。

> `异常也是接口的一部分`

```java
/**
 * This method does something extremely useful ...
 *
 * @param input
 * @throws MyBusinessException if ... happens
 */
public void doSomething(String input)throws MyBusinessException{
		...
		}
```

### 5 为异常添加描述性的消息

这条实践原则的背后基于这样的考虑：当异常发生时，不是每个人都能理解为什么会抛出这个异常，你应该为抛出的异常，添加更多准确的信息，描述发生了什么，当时的参数是什么等等。我喜欢这样一种比喻，我们去`debug`
问题的时候就像一个侦探，报出异常就像是犯罪现场的蛛丝马迹，所以越是尽可能准确的异常信息，越是能够帮助我们“破案”（fix bug）。

```java
throw new MyBusinessException(String.format("something happend,the input is %s",param))
```

### 6 优先 catch 具体异常，禁止 catch `Throwable`

越是具体的异常，越能够说明发生了什么问题，能更快的锁定现场和便于处理。

当你 catch 住某一个十分具体的异常的时候，你能够在很大程度上明白发生了什么，那么在随后就可以编写相应的处理代码。

```java
//尽量不要这么做
try {
   someMethod();
} catch (Exception e) {
   LOGGER.error("method has failed", e);
}
```

当你像上面这样去 catch 通用的 Exception 时，实际上隐藏了一些特殊情况的处理。比如在要 catch 的逻辑中会抛出 `checked exception`,
对这样的受检异常，你应该做一些处理的，但是你将它们与其他异常混作一起了，那么你的代码在某个时候就会发生意想不到的问题。

另外一点是，禁止 catch `Throwable`. 因为`Throwable` 还包含了 `Error`等 JVM 抛出无法处理的错误，对于这种情况，只能说明程序的运行环境已经不正常了，那就应该让它停止

### 7 要么用 log 记录异常，要么抛出异常，不要在一个 catch 中做这2件事情

```java
catch (NoSuchMethodException e) {
   LOGGER.error("Some information", e);
   throw e;
}
```

记录异常并抛出，会导致日志中出现重复的异常堆栈，不仅使日志量剧增，同时会使排查问题难以进行。

另外一点要注意的是，log 异常的正确方式是使用带有2个参数的方法，第二个参数是 Throwable（大多数日志框架都是如此）：

```java
LOGGER.error(String.format("message:%s",msg),error);

//不要这么做
LOGGER.error("message:{}，{}",msg,error);
```

### 8 正确地包装异常，使堆栈不会丢失

```java
public void wrapException()throws MyBusinessException{
		try{
		// do something
		}catch(NumberFormatException e){
		//切记将原始的异常传下去，不然就丢失了堆栈，难以排查
		throw new MyBusinessException("A message that describes the error.",e);
		}
		}
```

### 9 谨记 `Throw early，Catch late` 原则

这条原则是说，尽早将异常 `throw`出去，在得到足够多的信息能够处理异常的时候才处理它。有人可能会疑惑，到底什么时候处理异常呢？一般情况下，`异常的处理是在更高层的模块中进行的`。比如我们通常的 web 服务中，在
ControllerAdvice 这种最高层处理，然后转换成 Http 状态码返回给客户端，因为在高层模块才能够有足够的信息处理，转换成客户端理解的信息。

还有一点值得说的是，在我们编程的时候，很多时候要检查用户的输入，或是方法的入参，然后抛出异常，这里也是对应`throw early`
原则，尽早的发现问题然后抛出，以免执行了其他逻辑，一是无效的调用，而且还会增加开销。

### 10 不要使用异常机制来控制程序流程

这条实践说的是不要把 `try-catch`当作`if-else` 来做控制流程的事情，一是这样会使代码难以理解、阅读，二是异常情况下会有一些性能消耗，因为要记录当前的堆栈信息。