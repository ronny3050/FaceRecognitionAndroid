package com.example.debayan.facerecognitionandroid.Utils;

/**
 * Created by debayan on 5/21/17.
 */
public class TaskParams {
    String n;
    byte[] d;
    int f;
    boolean t;
    public TaskParams(String userName, byte[] data, int fc, boolean isTraining){
        this.n = userName;
        this.d = data;
        this.f = fc;
        this.t = isTraining;
    }
}
