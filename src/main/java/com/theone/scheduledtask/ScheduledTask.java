package com.theone.scheduledtask;

import com.theone.dingtalkrobot.DingTalkRobot;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-07-19 15:19
 */
public class ScheduledTask {

    public static void main(String[] args) throws SchedulerException {
        // 创建 SchedulerFactory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        // 获取 Scheduler
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 创建 JobDetail
        JobDetail updateTaskDetail = JobBuilder.newJob(updateTaskToastJob.class)
                .withIdentity("updateTaskToastJob", "group1")
                .build();

        // 创建 Trigger
        // 周一到周五，每天上午9:50触发
        Trigger updateTaskTrigger = TriggerBuilder.newTrigger()
                .withIdentity("updateTaskToastTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 50 9 ? * MON-FRI"))
                .build();

        JobDetail weeklyMeetingDetail = JobBuilder.newJob(weeklyMeetingToastJob.class)
                .withIdentity("weeklyMeetingToastJob", "group1")
                .build();

        // 每周四下午2:00触发
        Trigger weeklyMeetingTrigger = TriggerBuilder.newTrigger()
                .withIdentity("weeklyMeetingToastTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 14 ? * THU"))
                .build();

        // 为任务传递数组参数
        // 周会顺序：李冠鸿（13163716232）、张城魁（17591166598）、杨广润（18515817835）、夏艺丹（13182818573）、包敏璇（18006831823）
        weeklyMeetingDetail.getJobDataMap().put("phoneList",
                new String[]{"13163716232", "17591166598", "18515817835", "13182818573", "18006831823"});

        // 将 JobDetail 和 Trigger 注册到 Scheduler 中
        scheduler.scheduleJob(updateTaskDetail, updateTaskTrigger);
        scheduler.scheduleJob(weeklyMeetingDetail, weeklyMeetingTrigger);

        // 启动 Scheduler
        scheduler.start();
    }

    // 自定义Job类
    public static class updateTaskToastJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("updateTaskToastJob 定时任务执行，当前时间：" + sdf.format(new Date()));
            DingTalkRobot robot = new DingTalkRobot();
            robot.updateTaskToast();
        }
    }

    public static class weeklyMeetingToastJob implements Job {
        private static int index = 0;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            // 从任务参数中获取数组
            JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String[] phoneList = (String[]) dataMap.get("phoneList");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("weeklyMeetingToastJob 任务执行，当前时间：" + sdf.format(new Date()) +
                    "\n当前@的手机号：" + phoneList[index]);

            DingTalkRobot robot = new DingTalkRobot();
            robot.weeklyMeetingToast(phoneList[index]);

            // 更新索引变量，确保下次触发时读取下一个字符串
            index = (index + 1) % phoneList.length;
        }
    }
}
