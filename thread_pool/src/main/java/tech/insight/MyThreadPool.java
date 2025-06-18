package tech.insight;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyThreadPool {

    private int corePoolSize = 10;
    private int maxPoolSize = 16;
    BlockingQueue<Runnable> commandList = new ArrayBlockingQueue<>(1024);
    List<Thread> workerList = new ArrayList<>();
    List<Thread> supportList = new ArrayList<>();
    Runnable worker = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = commandList.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void execute(Runnable task) {
        if(workerList.size() < corePoolSize) {
            Thread worker = new Thread(task);
            workerList.add(worker);
            worker.start();
        }
        if(commandList.offer(task)) {
            return;
        }
        if(supportList.size() + workerList.size() < maxPoolSize) {
            Thread support = new Thread(task);
            supportList.add(support);
            support.start();
        }
        if(!commandList.offer(task)) {
            throw new RuntimeException("任务队列已满");
        }

    }

}
