package com.pikachu.convenientdelivery.IM.ui.bean;

public class Msg {

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String content;
    private String time;
    private int type;

    public Msg(String content,String time, int type) {
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public String getContent() {
        return content;
    }
    public String getTime() {
        return time;
    }
    public int getType() {
        return type;
    }

}
