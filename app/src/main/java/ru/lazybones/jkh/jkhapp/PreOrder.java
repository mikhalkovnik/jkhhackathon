package ru.lazybones.jkh.jkhapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class PreOrder {
    private String id;
    private String userphone;
    private String userid;
    private String objectid;
    private String adress;
    private int priority;
    private int  tipe;
    private String info;
    private String status;
    private String stage;
    private long time;
    private ArrayList<String> photourls;

    public PreOrder() {}


    public PreOrder(String userphone, String userid) {
            this.time = new Date().getTime();
         this.userphone = userphone;
         this.userid = userid;

     }


     public Map<String, Object> toMap() {
             HashMap<String, Object> result = new HashMap<>();
             result.put("userid", userid);
             result.put("userphone", userphone);
             result.put("objectid",  objectid);
             result.put("adress", adress);
             result.put("priority", priority);
         result.put("tipe", tipe);
         result.put("photourls", photourls);
         result.put("info", info);
         result.put("status",status);
         result.put("stage", stage);
         result.put("time", time);
         result.put("id", id);


         return result;
     }


     public String getUserphone() {
         return userphone;
     }

     public void setUserphone(String userphone) {
         this.userphone = userphone;
     }

     public String getUserid() {
         return userid;
     }

     public void setUserid(String userid) {
         this.userid = userid;
     }


    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<String> getPhotourls() {
        return photourls;
    }

    public void setPhotourls(ArrayList<String> photourls) {
        this.photourls = photourls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
