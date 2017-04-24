# Receptivity #
## 4/24 ##
* Exception java.lang.OutOfMemoryError
  * android:largeHeap="true"
* Exception java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask
  * you can reuse the schedule ,just cancel the running thread. the schedule can shutdown at the end of the application
