package com.protect.love.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class AlarmTaskLog {
    @Id
    @Property(nameInDb = "taskName")
    public String name;


    @Property(nameInDb = "year")
    public int year;


    @Property(nameInDb = "month")
    public int month;


    @Property(nameInDb = "day")
    public int day;

    @Property(nameInDb = "time")
    public long time;


    @Property(nameInDb = "hour")
    public int hour;


    @Property(nameInDb = "minute")
    public int minute;


    @Generated(hash = 376520388)
    public AlarmTaskLog(String name, int year, int month, int day, long time,
            int hour, int minute) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.hour = hour;
        this.minute = minute;
    }


    @Generated(hash = 1765859151)
    public AlarmTaskLog() {
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getYear() {
        return this.year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public int getMonth() {
        return this.month;
    }


    public void setMonth(int month) {
        this.month = month;
    }


    public int getDay() {
        return this.day;
    }


    public void setDay(int day) {
        this.day = day;
    }


    public long getTime() {
        return this.time;
    }


    public void setTime(long time) {
        this.time = time;
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

}
