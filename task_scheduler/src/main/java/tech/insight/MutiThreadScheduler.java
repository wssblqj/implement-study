package tech.insight;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

//手搓多线程调度器：最大并行执行2个任务，输入几个任务的 执行时间 & 任务名，输出任务执行完成的顺序，优先级为FIFO。
public class MutiThreadScheduler {

    public static void main(String[] args) {
        MutiThreadScheduler scheduler = new MutiThreadScheduler();
        scheduler.submit(new Job("task1", 1000));
        scheduler.submit(new Job("task2", 2000));
        scheduler.submit(new Job("task3", 3000));
        scheduler.submit(new Job("task4", 4000));
    }

    static class Job {
        private String name;
        private int duration;
        public Job(String name, int duration) {
            this.name = name;
            this.duration = duration;
        }
    }
    private Trigger trigger = new Trigger();
    public void submit(Job job) {
        trigger.jobList.offer(job);
        trigger.wakeUp();
    }

    {
        trigger.triggerThread.start();
    }

    ExecutorService executorService = Executors.newFixedThreadPool(2);


    private final List<String> taskOrder = new CopyOnWriteArrayList<>();

    class Trigger {
        //FIFO队列
        BlockingQueue<Job> jobList = new LinkedBlockingQueue<>();

        Thread triggerThread = new Thread(() -> {
            while (true) {
                while(jobList.isEmpty()) {
                    LockSupport.park();
                }
                Job job = jobList.poll();
                executorService.submit(() -> {
                    System.out.println(job.name + "start");
                    try {
                        Thread.sleep(job.duration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(job.name + "end");
                    taskOrder.add(job.name);
                });
            }
        });
        public void wakeUp() {
            LockSupport.unpark(triggerThread);
        }
    }
}
