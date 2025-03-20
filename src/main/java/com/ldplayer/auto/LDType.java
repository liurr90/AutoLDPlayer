package com.ldplayer.auto;

public enum LDType {
    NAME("name"),
    ID("id");

    private final String name;

    LDType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
} 