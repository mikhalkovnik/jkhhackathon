package ru.lazybones.jkh.jkhapp;

import java.util.HashMap;
import java.util.Map;

class User {
    private String userphone;
    private String userid;
    private String userpassword;
    private String userlogin;
    private String usermail;

    public User() {}


    public User(String userphone, String userid) {

         this.userphone = userphone;
         this.userid = userid;
     }


     public Map<String, Object> toMap() {
             HashMap<String, Object> result = new HashMap<>();
             result.put("userid", userid);
             result.put("userphone", userphone);
             result.put("userpassword", userpassword);
             result.put("userlogin", userlogin);
             result.put("usermail", usermail);



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

     public String getUserpassword() {
         return userpassword;
     }

     public void setUserpassword(String userpassword) {
         this.userpassword = userpassword;
     }

     public String getUserlogin() {
         return userlogin;
     }

     public void setUserlogin(String userlogin) {
         this.userlogin = userlogin;
     }

     public String getUsermail() {
         return usermail;
     }

     public void setUsermail(String usermail) {
         this.usermail = usermail;
     }
 }
