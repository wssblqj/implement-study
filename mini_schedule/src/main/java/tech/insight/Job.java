package tech.insight;

public class Job implements Comparable<Job>{

    private Long beginTime;

    private Runnable task;

    private Long delay;

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @Override
    public int compareTo(Job o) {
        return Long.compare(this.beginTime, o.beginTime);
    }
}
