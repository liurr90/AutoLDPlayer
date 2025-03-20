package com.ldplayer.auto;

public class LDevice {
    private int index;
    private String name;
    private long topHandle;
    private long bindHandle;
    private String adbId;

    public LDevice() {
    }

    public LDevice(int index, String name, long topHandle, long bindHandle) {
        this.index = index;
        this.name = name;
        this.topHandle = topHandle;
        this.bindHandle = bindHandle;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTopHandle() {
        return topHandle;
    }

    public void setTopHandle(long topHandle) {
        this.topHandle = topHandle;
    }

    public long getBindHandle() {
        return bindHandle;
    }

    public void setBindHandle(long bindHandle) {
        this.bindHandle = bindHandle;
    }

    public String getAdbId() {
        return adbId;
    }

    public void setAdbId(String adbId) {
        this.adbId = adbId;
    }

    @Override
    public String toString() {
        return "LDevice{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", topHandle=" + topHandle +
                ", bindHandle=" + bindHandle +
                ", adbId='" + adbId + '\'' +
                '}';
    }
} 