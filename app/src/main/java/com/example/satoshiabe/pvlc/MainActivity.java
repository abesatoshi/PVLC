package com.example.satoshiabe.pvlc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends Activity {

    final String TAG = MainActivity.class.getSimpleName();

    private Physicaloid mPhysicaloid;

    private Handler mHandler;

    private static final String ACTION_USB_PERMISSION = "USB_PERMISSION";

    private static final char DATA1 = '1';
    private static final char DATA2 = '2';
    private static final char DATA3 = '3';
    private static final char DATA4 = '4';
    private static final char DATA5 = '5';
    private static final char DATA6 = '8';
    private static final char DATA7 = '7';
    private static final char DATA8 = '6';
    private static final char DATA9 = '9';

    private static final String storage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PVLC/";
    private static final String VIDEO1 = storage + "SHelectivePlus.mp4";
    private static final String VIDEO2 = storage + "NaruhoButton.mp4";
    private static final String VIDEO3 = storage + "TrackyNotes.mp4";
    private static final String VIDEO4 = storage + "WriteMore.mp4";
    private static final String VIDEO5 = storage + "dePENd.mp4";
    private static final String VIDEO6 = storage + "MiragePrinter.mp4";
    private static final String VIDEO7 = storage + "HoVerTable.mp4";
    private static final String VIDEO8 = storage + "onNote.mp4";
    private static final String VIDEO9 = storage + "PVLC.mp4";

    private VideoView mVideoView;

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            initPhysicaloid();
                        }
                    } else {
                        Log.d(TAG, "permission denied");
                        Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhysicaloid = new Physicaloid(this);
        mHandler = new Handler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setMediaController(new MediaController(this));

        initPhysicaloid();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPhysicaloid.isOpened())
            mPhysicaloid.close();
        unregisterReceiver(mUsbReceiver);

        if (mVideoView.isPlaying())
            mVideoView.stopPlayback();

        mVideoView = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void initPhysicaloid() {

        if (!mPhysicaloid.open()) {
            Log.d(TAG, "open failed");
            Toast.makeText(this, "open failed",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "connected");
        Toast.makeText(this, "connected",Toast.LENGTH_SHORT).show();

        mPhysicaloid.addReadListener(new ReadLisener() {
            @Override
            public void onRead(int size) {
                byte buf[] = new byte[size];
                mPhysicaloid.read(buf, size);
                String str = new String();
                str += (char)buf[0];
                Log.d(TAG, str);
                final char data = (char)buf[0];

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (dataChanged(data)) {
                            Log.d(TAG, "data changed to " + data);
                            setVideo(data);
                        }
                    }
                });
            }
        });
    }

    char currentData = '0', prevData = '0';
    int dataContinue = 0;


    public boolean dataChanged(char data) {

        if (prevData == data)
            dataContinue++;
        else {
            dataContinue = 0;
            prevData = data;
        }

        if (dataContinue > 1 && data != currentData) {
            currentData = data;
            return true;
        }
        return false;
    }

    public void setVideo(char data) {
        switch (data) {
            case DATA1:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video1");
                mVideoView.setVideoPath(VIDEO1);
                mVideoView.start();
                Log.d(TAG, "started");
                break;
            case DATA2:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video2");
                mVideoView.setVideoPath(VIDEO2);
                mVideoView.start();
                break;
            case DATA3:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video3");
                mVideoView.setVideoPath(VIDEO3);
                mVideoView.start();
                break;
             case DATA4:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video4");
                mVideoView.setVideoPath(VIDEO4);
                mVideoView.start();
                break;
            case DATA5:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video5");
                mVideoView.setVideoPath(VIDEO5);
                mVideoView.start();
                break;
            case DATA6:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video6");
                mVideoView.setVideoPath(VIDEO6);
                mVideoView.start();
                break;
            case DATA7:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video7");
                mVideoView.setVideoPath(VIDEO7);
                mVideoView.start();
                break;
            case DATA8:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video8");
                mVideoView.setVideoPath(VIDEO8);
                mVideoView.start();
                break;
            case DATA9:
                if (mVideoView.isPlaying())
                    mVideoView.stopPlayback();
                Log.d(TAG, "set video9");
                mVideoView.setVideoPath(VIDEO9);
                mVideoView.start();
                break;
            default:
                break;
        }

    }



}
