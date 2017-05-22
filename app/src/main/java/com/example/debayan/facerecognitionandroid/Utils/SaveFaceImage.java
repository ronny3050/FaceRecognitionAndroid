package com.example.debayan.facerecognitionandroid.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cat.lafosca.facecropper.FaceCropper;

import static android.content.ContentValues.TAG;

public class SaveFaceImage extends AsyncTask<TaskParams,Void,Void> {

    @Override
    protected Void doInBackground(TaskParams... params) {
        String userName = params[0].n;
        int fc = params[0].f;
        byte[] data = params[0].d;
        boolean isTraining = params[0].t;
        File dataDirectory = new File(android.os.Environment
                .getExternalStorageDirectory().getPath() + "/FaceRecognitionAndroid/");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File dataDirectory2 = null;
        File tmpFile = null;
        if (isTraining) {
            dataDirectory2 = new File(android.os.Environment
                    .getExternalStorageDirectory().getPath() + "/FaceRecognitionAndroid/TrainingData/");
            if (!dataDirectory2.exists()) {
                dataDirectory2.mkdirs();
            }
            tmpFile = new File(dataDirectory2, Integer.toString(fc + 1) + ".jpg");
        } else {
            dataDirectory2 = new File(android.os.Environment
                    .getExternalStorageDirectory().getPath() + "/FaceRecognitionAndroid/TestingData/");
            if (!dataDirectory2.exists()) {
                dataDirectory2.mkdirs();
            }
        }

        Bitmap realImage = null;
        try {
            FileOutputStream fos = new FileOutputStream(tmpFile);

            realImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            ExifInterface exif = new ExifInterface(tmpFile.toString());

            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                realImage = rotate(realImage, 270);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                realImage = rotate(realImage, 180);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
                realImage = rotate(realImage, 270);
            }

            FaceCropper faceCropper = new FaceCropper();
            faceCropper.setFaceMarginPx(44);
            faceCropper.setMaxFaces(1);
            boolean bo = faceCropper.getCroppedImage(realImage).compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (!bo) {
                Log.e(TAG, "Failed to save jpg!");
            }

            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }

//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        if (!isTraining && faceCount == 1) {
//            Log.i("HierarchicalAuth", "Done collecting testing face data");
//            doneTestImage();
//        }
//    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}