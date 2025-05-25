package tech.insight;

import jdk.nashorn.internal.ir.CallNode;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class ScheduleService {
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private Trigger trigger = new Trigger();
    public void schedule(Runnable task, Long delay) {
        Job job = new Job();
        job.setTask(task);
        job.setDelay(delay);
        job.setBeginTime(System.currentTimeMillis() + delay);
        trigger.jobList.offer(job);
        trigger.wakeUp();
    }

    class Trigger {
        PriorityBlockingQueue<Job> jobList = new PriorityBlockingQueue<>();

        //调度线程
        Thread triggerThread = new Thread(() -> {
            while(true) {
                while(jobList.isEmpty()) {
                    LockSupport.park();
                }
                Job latestJob = jobList.peek();
                if(latestJob.getBeginTime() < System.currentTimeMillis()) {
                    latestJob = jobList.poll();
                    executorService.execute(latestJob.getTask());
                    //下一次任务
                    Job nextJob = new Job();
                    nextJob.setTask(latestJob.getTask());
                    nextJob.setDelay(latestJob.getDelay());
                    nextJob.setBeginTime(System.currentTimeMillis() + latestJob.getDelay());
                    jobList.offer(nextJob);
                } else {
                    LockSupport.parkUntil(latestJob.getBeginTime());
                }
            }
        });
        {
            triggerThread.start();
        }

        public void wakeUp() {
            LockSupport.unpark(triggerThread);
        }
    }


}

