package com.example.debayan.facerecognitionandroid;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debayan.facerecognitionandroid.Utils.DBHelper;
import com.example.debayan.facerecognitionandroid.Utils.SaveFaceImage;
import com.example.debayan.facerecognitionandroid.Utils.TaskParams;

/**
 * This class starts camera capture service to enroll user's faces.
 * After enrollment, face features are extracted for face recognition.
 *
 * @author debayan
 */

public class EnrollmentActivity extends Activity {

    private CameraPreview mPreview;
    private RelativeLayout mLayout;
    private Button enrollButton;
    private DBHelper dbHelper;
    private EditText userName;
    private TextView instructions;
    private Button submitButton;

    /*
    Constants
     */
    public static final String USER_SHAREDPREF_FILE = "UserPreferenceFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(getApplicationContext());

        // Hide status-bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Requires RelativeLayout.
        mLayout = new RelativeLayout(this);
        setContentView(mLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new CameraPreview(this, 1, CameraPreview.LayoutMode.FitToParent);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // Un-comment below lines to specify the size.
        //previewLayoutParams.height = 500;
        //previewLayoutParams.width = 500;

        // Un-comment below line to specify the position.
        //mPreview.setCenterPosition(270, 130);

        mLayout.addView(mPreview, 0, previewLayoutParams);

        // Add Enroll Button
        enrollButton = new Button(this);
        enrollButton.setText("Enroll");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLayout.addView(enrollButton,layoutParams);

        // Add Instructions text
        instructions = new TextView(this);
        instructions.setText("Enter user name below");
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.INVISIBLE);
        layoutParams.setMargins(0, 200, 0, 0);
        mLayout.addView(instructions, layoutParams);

        // Add userName EditText field
        userName = new EditText(this);
        userName.setHint("Enter user name");
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.INVISIBLE);
        mLayout.addView(userName, layoutParams);

        // Add Submit button
        submitButton = new Button(this);
        instructions.setText("Done");
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.INVISIBLE);
        layoutParams.setMargins(0, 15, 0, 0);
        mLayout.addView(submitButton, layoutParams);

        initViews();

        // If no user is found in the database, register new user
        if(dbHelper.userCount() == 0){
            // Hide camera preview and enroll button
            mPreview.setVisibility(View.INVISIBLE);
            enrollButton.setVisibility(View.INVISIBLE);

            // Show name editText
            instructions.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        }

    }

    private void initViews(){
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.mCamera.takePicture(null, null, mPicture);
            }
        });

        userName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getApplicationContext(), userName.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveFaceImage().execute(new TaskParams(data, 0, true));
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }

}
