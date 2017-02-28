package kr.co.marketlink.examples;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kr.co.marketlink.jsyang.CameraHelper;
import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.FileHelper;
import kr.co.marketlink.jsyang.PermissionHelper;

public class RecorderActivity extends AppCompatActivity implements View.OnClickListener{
    final int REQUEST_PERMISSION = 1;
    MediaRecorder recorder;
    File audiofile = null;
    Button startButton, stopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        startButton = (Button) findViewById(R.id.btn_start);
        stopButton = (Button) findViewById(R.id.btn_stop);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    public void startRecording() throws IOException {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".m4a", dir);
        } catch (IOException e) {
            Common.log("external storage access error");
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }

    public void stopRecording() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        //stopping recorder
        recorder.stop();
        recorder.release();
        //after stopping the recorder, create the sound file and add it to media library.
        addRecordingToMediaLibrary();
    }

    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/m4a");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        //sending broadcast message to scan the media file so that it can be available
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

        //Common.log("Added File " + FileHelper.uriToBase64(getApplicationContext(),newUri));
        String base64=FileHelper.uriToBase64(getApplicationContext(),newUri);
        String path=FileHelper.base64ToFile(base64,"test123.m4a");
        Uri myUri = FileHelper.pathToUri(path);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e){
            Common.log(e.toString());
        }

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_start){
            start();
        } else if(id==R.id.btn_stop){
            stopRecording();
        }
    }

    void start(){
        //권한 확인
        if (!PermissionHelper.hasPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)) {
            PermissionHelper.requestPermission(this, Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION);
            return;
        }
        try {
            startRecording();
        } catch(IOException e){
            Common.log(e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                try {
                    startRecording();
                } catch(IOException e){
                    Common.log(e.toString());
                }
                return;
            }
        }
    }

}