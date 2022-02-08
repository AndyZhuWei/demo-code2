####1.ExecutorService.submit() 方法返回的一直都是 Future 的实现类 FutureTask
####2.FutureTask实现了接口RunnableFuture，而RunnableFuture接口继承了Runnable和Future接口
####3.FutureTask有Callable参数得构造方法和Runnable参数得构造方法，即便是Runnable参数内部也会转换成Callable参数