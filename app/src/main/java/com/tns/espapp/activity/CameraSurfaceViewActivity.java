package com.tns.espapp.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CameraSurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    //  activity_camera_surface_view

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    Button edt_result_save;
    EditText edt_text;
    String getPicturePath ="";
    String current_date;
    private Calendar cal;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_surface_view);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        cal = Calendar.getInstance();
        current_date = dateFormat.format(cal.getTime());

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                String capturepath = "";
                String destinationpath = Environment.getExternalStorageDirectory().toString();
                SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
                String current_time_str = time_formatter.format(System.currentTimeMillis());

                File destination = new File(destinationpath + "/ESP/SurveyFormFixRow/");
                if (!destination.exists()) {
                    destination.mkdirs();
                }

                File file = null;
                FileOutputStream outStream = null;
                try {
                    capturepath = current_date + "_" + current_time_str + ".jpg";

                    file = new File(destination, capturepath);
                    outStream = new FileOutputStream(file);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                    Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                getPicturePath =destinationpath + "/ESP/SurveyFormFixRow/" + capturepath;
                Log.d("Log", getPicturePath);
                       // refreshCamera();
            }
        };
        edt_text =findViewById(R.id.edt_text);
        edt_result_save=findViewById(R.id.edt_result_save);
        edt_result_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=edt_text.getText().toString();
                Intent intent=new Intent();

                intent.putExtra("text",message);
                intent.putExtra("path",getPicturePath);
                setResult(2,intent);
                finish();//finishing activity
            }
        });
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.

        refreshCamera();
        camera.setDisplayOrientation(90);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();

        // modify parameter
        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}