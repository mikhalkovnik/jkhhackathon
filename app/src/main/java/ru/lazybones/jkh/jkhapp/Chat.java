package ru.lazybones.jkh.jkhapp;

import java.util.HashMap;
import java.util.Map;

public class Chat {
    private String user1uid;
    private String user2uid;
    private String lastmessage;
    private boolean online;
    private String username;
    private long time;
    private String userPhotourl;

    public Chat() {}


    public Chat(User user1, User user2) {
        this.user1uid= user1.getUserid();
        this.user2uid = user2.getUserid();
        this.username = user2.getUserlogin();

    }

    public Chat(User user1, User user2, Message lastmessage ) {
        this.user1uid= user1.getUserid();
        this.user2uid = user2.getUserid();
        this.username = user2.getUserlogin();
        this.lastmessage = lastmessage.getText();
        this.time = lastmessage.getTime();
        //this.userPhotourl = user2.getPhotoUrl();
    }

    public Chat(String user1id, String user2,String username , Message lastmessage ) {
        this.user1uid= user1id;
        this.user2uid =user2;
        this.username = username;
        this.lastmessage = lastmessage.getText();
        this.time = lastmessage.getTime();
        //this.userPhotourl = user2.getPhotoUrl();
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user1uid", user1uid);
        result.put("user2uid", user2uid);
        result.put("username", username);
        result.put("online", online);
        result.put("lastmessage", lastmessage);
        //result.put("userPhotourl", userPhotourl);

        return result;
    }

    public String getUser1uid () {        return user1uid;    }

    public void setUser1uid(String user1uid) {
        this.user1uid = user1uid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getUser2uid() {
        return user2uid;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserPhotourl() {
        return userPhotourl;
    }

    public void setUserPhotourl(String userPhotourl) {
        this.userPhotourl = userPhotourl;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public void setUser2uid(String user2uid) {
        this.user2uid = user2uid;
    }

    public long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
