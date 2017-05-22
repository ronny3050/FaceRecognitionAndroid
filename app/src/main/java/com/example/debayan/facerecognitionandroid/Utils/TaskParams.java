package com.example.debayan.facerecognitionandroid.Utils;

/**
 * Created by debayan on 5/21/17.
 */
public class TaskParams {
    byte[] d;
    int f;
    boolean t;
    public TaskParams(byte[] data, int fc, boolean isTraining){
        this.d = data;
        this.f = fc;
        this.t = isTraining;
    }
}
