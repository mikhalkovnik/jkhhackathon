package ru.lazybones.jkh.jkhapp;

import java.util.ArrayList;

public class Buffer {
    public static User user;
    public static User curuser;
    public static Chat chat;
    public static final String photourlpart1 = "https://firebasestorage.googleapis.com/v0/b/neft-d9691.appspot.com/o/userspics%2F";
    public static final String photourlpart2 = ".jpg?alt=media&token=a4840188-3234-45f8-8710-55124af7c9bc";
    public static Class back;
    public static ArrayList<Operation> history;
    public static int money;



//    public static void moneyupdate() {
//        int m=0;
//        for (Operation op: history) {
//            if (op.getPlus())
//            m=m+op.getAmount();
//            else m=m-op.getAmount();
//        }
//        money=m;
//    }

}
