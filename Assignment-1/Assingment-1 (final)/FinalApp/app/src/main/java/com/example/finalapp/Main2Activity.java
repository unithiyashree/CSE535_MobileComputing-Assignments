package com.example.finalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static Map<String,String> getVideo = new HashMap<>();
    String videopath = null;
    VideoView mVideoView = null;
    File directory;
    final String endpath = "_mc16.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getVideo.put("explain","https://www.signingsavvy.com/media/mp4-ld/22/22623.mp4");
        getVideo.put("book","https://www.signingsavvy.com/media/mp4-ld/14/14326.mp4");
        getVideo.put("car","https://www.signingsavvy.com/media/mp4-ld/26/26165.mp4");
        getVideo.put("future","https://www.signingsavvy.com/media/mp4-ld/14/14736.mp4");
        getVideo.put("gift","https://www.signingsavvy.com/media/mp4-ld/23/23781.mp4");
        getVideo.put("good","https://www.signingsavvy.com/media/mp4-ld/21/21534.mp4");
        getVideo.put("learn","https://www.signingsavvy.com/media/mp4-ld/21/21560.mp4");
        getVideo.put("like","https://www.signingsavvy.com/media/mp4-ld/6/6394.mp4");
        getVideo.put("movie","https://www.signingsavvy.com/media/mp4-ld/8/8626.mp4");
        getVideo.put("now","https://www.signingsavvy.com/media/mp4-ld/7/7774.mp4");
        getVideo.put("pay","https://www.signingsavvy.com/media/mp4-ld/14/14618.mp4");
        getVideo.put("pet","https://www.signingsavvy.com/media/mp4-ld/25/25066.mp4");
        getVideo.put("sell","https://www.signingsavvy.com/media/mp4-ld/9/9199.mp4");
        getVideo.put("should","https://www.signingsavvy.com/media/mp4-ld/9/9563.mp4");
        getVideo.put("thank you","https://www.signingsavvy.com/media/mp4-ld/21/21533.mp4");
        getVideo.put("that","https://www.signingsavvy.com/media/mp4-ld/14/14366.mp4");
        getVideo.put("total","https://www.signingsavvy.com/media/mp4-ld/26/26467.mp4");
        getVideo.put("work","https://www.signingsavvy.com/media/mp4-ld/14/14523.mp4");
        getVideo.put("trip","https://www.signingsavvy.com/media/mp4-ld/14/14645.mp4");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        System.out.println(directory);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        if(!directory.exists()){
            directory.mkdir();
        }

        Bundle selectedItem = getIntent().getExtras();
        final String lastname = selectedItem.getString("lastname");
        final String VideoSelected = selectedItem.getString("Vid");

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/" + VideoSelected + "_mc18.mp4");
        if(file.exists()){
            System.out.println("yes file");
            run();
        }
        else{
            System.out.println("no file");
            DownloadVideo downloadVideo = new DownloadVideo(
                    VideoSelected+endpath,directory,Main2Activity.this,getVideo.get(VideoSelected));
            downloadVideo.execute("");
        }
    }


    public void run(){
        Bundle selectedItem = getIntent().getExtras();
        final String VideoSelected = selectedItem.getString("Vid");
        MediaController mc = new MediaController(this);
        mc.setAnchorView(mVideoView);
        mc.setMediaPlayer(mVideoView);
        String videoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator + VideoSelected + "_mc16.mp4";
        System.out.println("Video path " + videoPath);
        Uri video = Uri.parse(videoPath);
        mVideoView.setMediaController(mc);
        mVideoView.setVideoURI(video);
        mVideoView.start();
        final Button practiceButton = (Button) findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Bundle selectedItem = getIntent().getExtras();
                final String lastname = selectedItem.getString("lastname");
                final String Vid = selectedItem.getString("Vid");
                Intent intent = new Intent( Main2Activity.this, Main3Activity.class);
                Bundle basket= new Bundle();
                basket.putString("lastname", lastname);
                basket.putString("aslVid", Vid);
                intent.putExtras(basket);
                startActivity( intent);

            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getFilePath(String fileName) {

        String videoPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                + "/" + fileName;


        File videoFile = new File(videoPath);
        if (videoFile.exists()) {
            videoFile.delete();
        }

        String saveFileDirectory = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();


        File directory = new File(saveFileDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }


        if (!videoFile.exists()) {
            try {
                videoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return videoPath;
    }
}

class DownloadVideo extends AsyncTask<String, Void, Boolean> {

    public static final String TAG = "DOWNLOAD_DB";

    Main2Activity mActivity;
    private boolean downloaded = true;

    public static  String DOWNLOAD_URI = null;

    File downloadFilePath;
    String downloadFileName;

    public DownloadVideo(String fileName, File filePath, Main2Activity mActivity, String Uri) {
        downloadFileName = fileName;
        downloadFilePath = filePath;
        this.mActivity = mActivity;
        DOWNLOAD_URI = Uri;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        download(downloadFileName, downloadFilePath);
        return true;
    }

    private void download(String downloadFileName, File downloadFilePath) {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
            downloaded = false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            downloaded = false;
        }

        try {
            URL url = new URL(DOWNLOAD_URI);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            InputStream inputStream = urlConnection.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File(downloadFilePath, downloadFileName));

            try {
                byte[] buffer = new byte[1024];
                int bufferLength;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    Log.i(TAG, Integer.valueOf(bufferLength).toString());
                    outputStream.write(buffer, 0, bufferLength);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                outputStream.close();
                downloaded = false;
            }

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            Log.e("MalformedURLException", "error: " + e.getMessage(), e);
            downloaded = false;
        } catch (final IOException e) {
            e.printStackTrace();
            Log.e("IOException", "error: " + e.getMessage(), e);
            downloaded = false;
        } catch (final Exception e) {
            Log.e(e.getMessage(), String.valueOf(e));
            downloaded = false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (downloaded) {
            mActivity.run();
            Toast.makeText(mActivity.getApplicationContext(), "File Downloaded", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity.getApplicationContext(), "Unable to Download File", Toast.LENGTH_LONG).show();
        }
    }
}


