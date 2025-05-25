package tech.insight;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ScheduleService scheduleService = new ScheduleService();
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss SSS")) + "这是100ms的任务");
        }, 100L);
        Thread.sleep(500L);
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss SSS")) + "这是500ms的任务");
        }, 500L);

    }
}
