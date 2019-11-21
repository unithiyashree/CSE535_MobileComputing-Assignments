package com.example.finalapp;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.finalapp.Main3Activity.SERVER_URI;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int Count;
    public static final String SERVER_URI = "http://10.218.107.121/cse535/upload_video.php";

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;
    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_main3);
        prefs = getPreferences( Context.MODE_PRIVATE);
        editor = prefs.edit();

        Button captureButton = (Button) findViewById(R.id.capture);

        if(!hasCamera()){
            captureButton.setEnabled(false);
        }

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRecording();
            }
        });
    }

    public void startRecording()
    {
        Count = prefs.getInt("counter", 0);
        Count ++;
        editor.putInt("counter", Count);
        editor.commit();
        Bundle selectedItem = getIntent().getExtras();
        final String lastname = selectedItem.getString("lastname");
        final String VideoSelected = selectedItem.getString("Vid");
        final File mediaFile = new
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/" +"GESTURE_PRACTICE_" + Count +"_"+ lastname + "_mc16.mp4");

        final Button uploadButton = (Button) findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                try {
                    UploadVideo uploadVideo = new UploadVideo(Main3Activity.this);
                    uploadVideo.execute(mediaFile);
                    Intent intent = new Intent(Main3Activity.this,MainActivity.class);
                    startActivity( intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //
            }
        });

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
        fileUri = FileProvider.getUriForFile(Main3Activity.this, BuildConfig.APPLICATION_ID + ".provider", mediaFile);
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(videoIntent, VIDEO_CAPTURE);
    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            return false;
        }
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                System.out.println(data.getData());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onClick(View view) {

    }

}

class UploadVideo extends AsyncTask<File, Void, Void> {

    public static final String UPLOAD_URI = SERVER_URI +"/UploadToServer.php";


    Main3Activity mActivity;
    private boolean uploaded = true;

    public UploadVideo(Main3Activity mainActivity) {
        this.mActivity = mainActivity;
    }


    @Override
    protected Void doInBackground(File... params) {
        upload(params[0]);
        return null;
    }

    private int upload(File sourceFileUri) {

        ProgressDialog progressDialog = null;
        int serverResponseCode = 0;

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
            uploaded = false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            uploaded = false;
        }

        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1048576;
        File sourceFile = new File(String.valueOf(sourceFileUri));
        if (!sourceFile.isFile()) {
            progressDialog.dismiss();
            Log.e("UploadFile", "Source File not exist :");
        } else {
            try {

                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URI);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + sourceFileUri + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = httpURLConnection.getResponseCode();
                String serverResponseMessage = httpURLConnection.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                uploaded = false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
                uploaded = false;
            }
        }
        return serverResponseCode;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (uploaded) {
            Toast.makeText(mActivity.getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity.getApplicationContext(), "Failed to upload file", Toast.LENGTH_SHORT).show();
        }
    }
}


