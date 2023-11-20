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


        /*
        测试部门定时任务：每日飞书文档更新
         */
        JobDetail updateTaskDetail = JobBuilder.newJob(updateTaskToastJob.class)
                .withIdentity("updateTaskToastJob", "group1")
                .build();
        // 周一到周五，每天上午9:50触发
        Trigger updateTaskTrigger = TriggerBuilder.newTrigger()
                .withIdentity("updateTaskToastTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 50 9 ? * MON-FRI"))
                .build();

        /*
        测试部门定时任务：周会提醒
         */
        JobDetail weeklyMeetingDetail = JobBuilder.newJob(weeklyMeetingToastJob.class)
                .withIdentity("weeklyMeetingToastJob", "group1")
                .build();
        // 每周五上午10:00触发
        Trigger weeklyMeetingTrigger = TriggerBuilder.newTrigger()
                .withIdentity("weeklyMeetingToastTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 ? * FRI"))
                .build();
        // 周会顺序：苏明辉
        weeklyMeetingDetail.getJobDataMap().put("phoneList",
                new String[]{"18867521753"});

        /*
        电商值班定时任务
         */
        JobDetail dianShangDetail = JobBuilder.newJob(dianShangZhiBanJob.class)
                .withIdentity("dianShangZhiBanJob", "group1")
                .build();
        // 每周一上午10:00触发
        Trigger dianShangTrigger = TriggerBuilder.newTrigger()
                .withIdentity("dianShangTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 ? * MON"))
                .build();
        // 值班顺序：李冠鸿（13163716232）、夏艺丹（13182818573）、包敏璇（18006831823）
        dianShangDetail.getJobDataMap().put("dianShangPhoneList",
                new String[]{"13163716232", "13182818573", "18006831823"});

        /*
        唯艺云值班定时任务
         */
        JobDetail weiYiYunDetail = JobBuilder.newJob(weiYiYunZhiBanJob.class)
                .withIdentity("weiYiYunZhiBanJob", "group1")
                .build();
        // 每周一上午10:00触发
        Trigger weiYiYunTrigger = TriggerBuilder.newTrigger()
                .withIdentity("weiYiYunTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 ? * MON"))
                .build();
        // 值班顺序：包敏璇（18006831823）
        weiYiYunDetail.getJobDataMap().put("weiYiYunPhoneList",
                new String[]{"18006831823"});


        // 将 JobDetail 和 Trigger 注册到 Scheduler 中
        scheduler.scheduleJob(updateTaskDetail, updateTaskTrigger);
        scheduler.scheduleJob(weeklyMeetingDetail, weeklyMeetingTrigger);
        scheduler.scheduleJob(dianShangDetail, dianShangTrigger);
//        scheduler.scheduleJob(weiYiYunDetail, weiYiYunTrigger);  // 故障群已合并为一个

        // 启动 Scheduler
        scheduler.start();
    }

    /**
     * 每日飞书文档更新job
     */
    public static class updateTaskToastJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("updateTaskToastJob 定时任务执行，当前时间：" + sdf.format(new Date()));
            DingTalkRobot robot = new DingTalkRobot();
            robot.updateTaskToast();
        }
    }

    /**
     * 每周周会提醒job
     */
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

    /**
     * 电商故障群值班人员提醒job
     */
    public static class dianShangZhiBanJob implements Job {
        private static int index = 0;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            // 从任务参数中获取数组
            JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String[] phoneList = (String[]) dataMap.get("dianShangPhoneList");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("dianShangZhiBanJob 任务执行，当前时间：" + sdf.format(new Date()) +
                    "\n当前@的手机号：" + phoneList[index]);

            DingTalkRobot robot = new DingTalkRobot();
            robot.dianShangZhiBan(phoneList[index]);

            // 更新索引变量，确保下次触发时读取下一个字符串
            index = (index + 1) % phoneList.length;
        }
    }

    /**
     * 唯艺云故障群值班人员提醒job
     */
    public static class weiYiYunZhiBanJob implements Job {
        private static int index = 0;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            // 从任务参数中获取数组
            JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String[] phoneList = (String[]) dataMap.get("weiYiYunPhoneList");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("weiYiYunZhiBanJob 任务执行，当前时间：" + sdf.format(new Date()) +
                    "\n当前@的手机号：" + phoneList[index]);

            DingTalkRobot robot = new DingTalkRobot();
            robot.weiYiYunZhiBan(phoneList[index]);

            // 更新索引变量，确保下次触发时读取下一个字符串
            index = (index + 1) % phoneList.length;
        }
    }
}
