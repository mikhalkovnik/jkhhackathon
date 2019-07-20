package ru.lazybones.jkh.jkhapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Current {
    public static int uidnumber=0;
    public static String uidstring ="";
    public static String username = "";
    public static String userUid="";
    public static String url="";
    public static String key="";
    public static List<String> options;
    public static String phonenumber="";
    public static ArrayList<PreOrder> preOrders;
    public static PreOrder preOrder;


    public static int getResId(String resName, Class<?> c) {
        try { Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }





}
