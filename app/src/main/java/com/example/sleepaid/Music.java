package com.example.sleepaid;


//Music 类型文件，用于集合musics，声明一个类型，获取集合中的名字、路径
public class Music {
    //name 类型
    private String name;
    private String duration;
    private int intDuration;
    private String rawName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setIntDuration(int intDuration) {
        this.intDuration = intDuration;
    }

    public  int getIntDuration(){
        return intDuration;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name  + '}';
    }
}


