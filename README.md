## 定时任务
### jar包位置：47.104.170.207 服务器的 /usr/local/src/testGroup 文件夹下

### 启动命令：nohup java -Xms1024m -Xmx1024m -cp ./scheduledTask-jar-with-dependencies.jar com.theone.scheduledtask.ScheduledTask  > ./scheduledTask.log 2>&1 &

### 查询进程：ps -ef | grep "java -Xms1024m -Xmx1024m -cp ./scheduledTask-jar-with-dependencies.jar com.theone.scheduledtask.ScheduledTask" | grep -v grep