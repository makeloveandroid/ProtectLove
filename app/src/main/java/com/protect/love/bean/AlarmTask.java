package com.protect.love.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class AlarmTask {
    public static final String ONE_DATA_MSG_TASK_NAME = "ONE_DATA_MSG_TASK_NAME";
    public static final String LOVE_MSG_TASK_NAME = "LOVE_MSG_TASK_NAME";
    public static final String TODAY_WEATHER_TASK_NAME = "TODAY_WEATHER_TASK_NAME";
    public static final String TODAY_CUSTOM_TASK_NAME = "TODAY_CUSTOM_TASK_NAME";


    @Id
    @Property(nameInDb = "taskName")
    public String name;


    @Property(nameInDb = "IsOpen")
    public boolean IsOpen;


    @Property(nameInDb = "hour")
    public int hour;


    @Property(nameInDb = "minute")
    public int minute;


    @Generated(hash = 588927544)
    public AlarmTask(String name, boolean IsOpen, int hour, int minute) {
        this.name = name;
        this.IsOpen = IsOpen;
        this.hour = hour;
        this.minute = minute;
    }


    @Generated(hash = 1393163738)
    public AlarmTask() {
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public boolean getIsOpen() {
        return this.IsOpen;
    }


    public void setIsOpen(boolean IsOpen) {
        this.IsOpen = IsOpen;
    }


    public int getHour() {
        return this.hour;
    }


    public void setHour(int hour) {
        this.hour = hour;
    }


    public int getMinute() {
        return this.minute;
    }


    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "AlarmTask{" +
                "name='" + name + '\'' +
                ", IsOpen=" + IsOpen +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }


}
