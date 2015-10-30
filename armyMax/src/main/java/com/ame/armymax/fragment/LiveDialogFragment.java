package com.ame.armymax.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;

public class LiveDialogFragment extends BlurDialogFragment implements View.OnClickListener {
    private final static String LOG_TAG = "MainActivity";

    private PowerManager.WakeLock mWakeLock;

    // private String ffmpeg_link =
    // "rtmp://live:live@128.122.151.108:1935/live/test.flv";
    private String ffmpeg_link = "rtmp://" + Data.LIVE + "/live/"
            + DataUser.VM_USER_NAME;
    // private String ffmpeg_link = "/mnt/sdcard/new_stream.flv";

    private volatile FFmpegFrameRecorder recorder;
    boolean recording = false;
    long startTime = 0;

    private int sampleAudioRateInHz = 44100;
    private int imageWidth = 640;
    private int imageHeight = 480;
    private int frameRate = 30;

    private Thread audioThread;
    volatile boolean runAudioThread = true;
    private AudioRecord audioRecord;
    private AudioRecordRunnable audioRecordRunnable;

    private CameraView cameraView;
    private opencv_core.IplImage yuvIplimage = null;

    private Button recordButton;
    private RelativeLayout mainLayout;

    Animation animation;
    TextView liveLabel;
    String title;
    String desc;
    Context context;

    View view;

    public static LiveDialogFragment newInstance(String title,
                                                   String desc) {
        LiveDialogFragment fragment = new LiveDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("desc",desc);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        title = args.getString(title);
        desc = args.getString(desc);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        /*
        // Remove title bar
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
 */

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



		/*
		 * Display display = getWindowManager().getDefaultDisplay(); Point size
		 * = new Point(); display.getSize(size); imageWidth = (int) (size.x *
		 * 0.5); imageHeight = (int) (imageWidth * 0.75);
		 */

        if (isTablet(getActivity())) {
            imageWidth = 960;
            imageHeight = 720;
        } else {
            imageWidth = 480;
            imageHeight = 320;
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.activity_live_now, null);
        builder.setView(view);



        ButterKnife.inject(this, view);

        initLayout(view);
        initRecorder();



        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        Dialog d = getDialog();
        if (d != null) {
            int width = 1000;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
        */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDialog().getWindow().setLayout(imageWidth + 120, imageHeight);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecording();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void postLive() {
        String url = "https://www.armymax.com/api/?action=postLive";

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("token", DataUser.VM_USER_TOKEN);
        params.put("title", title);
        params.put("desc", "ถ่ายทอดสดโดย " + DataUser.VM_NAME);
        params.put("source", "rtmp://" + Data.LIVE + "/live/"
                + DataUser.VM_USER_NAME);

        AQuery aq = new AQuery(context);

        aq.ajax(url, params, JSONObject.class, this, "liveCb");

    }

    public void liveCb(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            if (json.optInt("status") == 5101) {
                Toast.makeText(context, json.optString("msg"),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, json.optString("msg"),
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    private void initLayout(View view) {

        mainLayout = (RelativeLayout) view.findViewById(R.id.record_layout);

        recordButton = (Button) view.findViewById(R.id.recordButton);
        // recordButton = new Button(this);
        // recordButton.setText("Start");
        recordButton.setOnClickListener(this);

        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible
        // to invisible
        animation.setDuration(300); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        // animation
        // rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        // infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        // end so the button will
        // fade back in

        liveLabel = (TextView) view.findViewById(R.id.liveLabel);

        cameraView = new CameraView(getActivity());
        // cameraView = (CameraView) findViewById(R.id.surface_camera);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                imageWidth , imageHeight);
        FrameLayout liveFrame = (FrameLayout) view.findViewById(R.id.live_frame);
        liveFrame.addView(cameraView, 0, layoutParam);
        // mainLayout.addView(cameraView, 0, layoutParam);
        // mainLayout.addView(recordButton);
        Log.v(LOG_TAG, "added cameraView to mainLayout");
    }

    private void initRecorder() {
        Log.w(LOG_TAG, "initRecorder");

        if (yuvIplimage == null) {
            // Recreated after frame size is set in surface change method
            yuvIplimage = opencv_core.IplImage.create(imageWidth, imageHeight,
                    IPL_DEPTH_8U, 2);
            // yuvIplimage = IplImage.create(imageWidth, imageHeight,
            // IPL_DEPTH_32S, 2);

            Log.v(LOG_TAG, "IplImage.create");
        }

        recorder = new FFmpegFrameRecorder(ffmpeg_link, imageWidth,
                imageHeight, 1);
        Log.v(LOG_TAG, "FFmpegFrameRecorder: " + ffmpeg_link + " imageWidth: "
                + imageWidth + " imageHeight " + imageHeight);

        recorder.setFormat("flv");
        Log.v(LOG_TAG, "recorder.setFormat(\"flv\")");

        recorder.setSampleRate(sampleAudioRateInHz);
        Log.v(LOG_TAG, "recorder.setSampleRate(sampleAudioRateInHz)");

        // re-set in the surface changed method as well
        recorder.setFrameRate(frameRate);
        Log.v(LOG_TAG, "recorder.setFrameRate(frameRate)");

        // Create audio recording thread
        audioRecordRunnable = new AudioRecordRunnable();
        audioThread = new Thread(audioRecordRunnable);
    }

    // Start the capture
    public void startRecording() {

        try {
            if (recorder != null) {
                recorder.start();
                startTime = System.currentTimeMillis();
                recording = true;
                // if(!runAudioThread)
                runAudioThread = true;
                audioThread.start();
                postLive();
                recordButton.startAnimation(animation);

            } else {
                initRecorder();
                recorder.start();
                startTime = System.currentTimeMillis();
                recording = true;
                // if(!runAudioThread)
                runAudioThread = true;
                audioThread.start();
                recordButton.startAnimation(animation);

            }

        } catch (FFmpegFrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        // This should stop the audio thread from running
        runAudioThread = false;

        if (recorder != null && recording) {
            recording = false;
            Log.v(LOG_TAG,
                    "Finishing recording, calling stop and release on recorder");
            try {
                recorder.stop();
                recorder.release();
                // audioThread.stop();
                recordButton.setVisibility(View.VISIBLE);


            } catch (FFmpegFrameRecorder.Exception e) {
                e.printStackTrace();
            }
            recorder = null;
        } else {
            recording = false;

        }
    }



    @Override
    public void onClick(View v) {
        if (!recording) {
            startRecording();
            Log.w(LOG_TAG, "Start Button Pushed");
            // recordButton.setText("Stop");
            Resources res = getResources();
            Drawable redBg = res.getDrawable(R.drawable.live_red_bg);
            liveLabel.setBackground(redBg);
            liveLabel.setText("Live!");

        } else {
            recordButton.setVisibility(View.INVISIBLE);
            stopRecording();
            Log.w(LOG_TAG, "Stop Button Pushed");
            // recordButton.setText("Start");
            v.clearAnimation();
            Resources res = getResources();
            Drawable orangeBg = res.getDrawable(R.drawable.live_orange_bg);
            liveLabel.setBackground(orangeBg);
            liveLabel.setText("Ready");
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    // ---------------------------------------------
    // audio thread, gets and encodes audio data
    // ---------------------------------------------
    class AudioRecordRunnable implements Runnable {

        @Override
        public void run() {
            // Set the thread priority
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            // Audio
            int bufferSize;
            short[] audioData;
            int bufferReadResult;

            bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleAudioRateInHz,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);

            audioData = new short[bufferSize];

            Log.d(LOG_TAG, "audioRecord.startRecording()");
            audioRecord.startRecording();

            // Audio Capture/Encoding Loop
            while (runAudioThread) {
                // Read from audioRecord
                bufferReadResult = audioRecord.read(audioData, 0,
                        audioData.length);
                if (bufferReadResult > 0) {
                    // Log.v(LOG_TAG,"audioRecord bufferReadResult: " +
                    // bufferReadResult);

                    // Changes in this variable may not be picked up despite it
                    // being "volatile"
                    if (recording) {
                        try {
                            // Write to FFmpegFrameRecorder
                            Buffer[] buffer = { ShortBuffer.wrap(audioData, 0,
                                    bufferReadResult) };
                            recorder.record(buffer);
                        } catch (FFmpegFrameRecorder.Exception e) {
                            Log.v(LOG_TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "AudioThread Finished");

            // com.example.javacv.stream.test2.LiveStreamingActivity.CameraView
			/* Capture/Encoding finished, release recorder */
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.v(LOG_TAG, "audioRecord released");
            }
        }
    }

    class CameraView extends SurfaceView implements SurfaceHolder.Callback,
            Camera.PreviewCallback {

        private boolean previewRunning = false;

        private SurfaceHolder holder;
        private Camera camera;

        private byte[] previewBuffer;

        long videoTimestamp = 0;

        Bitmap bitmap;
        Canvas canvas;

        public CameraView(Context _context) {
            super(_context);

            holder = this.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public CameraView(Context context, AttributeSet attrs) {
            super(context, attrs);

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(holder);
                camera.setPreviewCallback(this);

                Camera.Parameters currentParams = camera.getParameters();
                Log.v(LOG_TAG,
                        "Preview Framerate: "
                                + currentParams.getPreviewFrameRate());
                Log.v(LOG_TAG,
                        "Preview imageWidth: "
                                + currentParams.getPreviewSize().width
                                + " imageHeight: "
                                + currentParams.getPreviewSize().height);

                // Use these values
                imageWidth = currentParams.getPreviewSize().width;
                imageHeight = currentParams.getPreviewSize().height;
                frameRate = currentParams.getPreviewFrameRate();

                bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                        Bitmap.Config.ALPHA_8);

				/*
				 * Log.v(LOG_TAG,"Creating previewBuffer size: " + imageWidth *
				 * imageHeight *
				 * ImageFormat.getBitsPerPixel(currentParams.getPreviewFormat
				 * ())/8); previewBuffer = new byte[imageWidth * imageHeight *
				 * ImageFormat
				 * .getBitsPerPixel(currentParams.getPreviewFormat())/8];
				 * camera.addCallbackBuffer(previewBuffer);
				 * camera.setPreviewCallbackWithBuffer(this);
				 */

                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                Log.v(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.v(LOG_TAG, "Surface Changed: width " + width + " height: "
                    + height);

            // We would do this if we want to reset the camera parameters
			/*
			 * if (!recording) { if (previewRunning){ camera.stopPreview(); }
			 *
			 * try { //Camera.Parameters cameraParameters =
			 * camera.getParameters(); //p.setPreviewSize(imageWidth,
			 * imageHeight); //p.setPreviewFrameRate(frameRate);
			 * //camera.setParameters(cameraParameters);
			 *
			 * camera.setPreviewDisplay(holder); camera.startPreview();
			 * previewRunning = true; } catch (IOException e) {
			 * Log.e(LOG_TAG,e.getMessage()); e.printStackTrace(); } }
			 */

            // Get the current parameters
            Camera.Parameters currentParams = camera.getParameters();
            Log.v(LOG_TAG,
                    "Preview Framerate: " + currentParams.getPreviewFrameRate());
            Log.v(LOG_TAG,
                    "Preview imageWidth: "
                            + currentParams.getPreviewSize().width
                            + " imageHeight: "
                            + currentParams.getPreviewSize().height);

            // Use these values
            imageWidth = currentParams.getPreviewSize().width;
            imageHeight = currentParams.getPreviewSize().height;
            frameRate = currentParams.getPreviewFrameRate();

            // Create the yuvIplimage if needed
            yuvIplimage = opencv_core.IplImage.create(imageWidth, imageHeight,
                    IPL_DEPTH_8U, 2);
            // yuvIplimage = IplImage.create(imageWidth, imageHeight,
            // IPL_DEPTH_32S, 2);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                camera.setPreviewCallback(null);

                previewRunning = false;
                camera.release();

            } catch (RuntimeException e) {
                Log.v(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            if (yuvIplimage != null && recording) {
                videoTimestamp = 1000 * (System.currentTimeMillis() - startTime);

                // Put the camera preview frame right into the yuvIplimage
                // object
                yuvIplimage.getByteBuffer().put(data);

                // FAQ about IplImage:
                // - For custom raw processing of data, getByteBuffer() returns
                // an NIO direct
                // buffer wrapped around the memory pointed by imageData, and
                // under Android we can
                // also use that Buffer with Bitmap.copyPixelsFromBuffer() and
                // copyPixelsToBuffer().
                // - To get a BufferedImage from an IplImage, we may call
                // getBufferedImage().
                // - The createFrom() factory method can construct an IplImage
                // from a BufferedImage.
                // - There are also a few copy*() methods for
                // BufferedImage<->IplImage data transfers.

                // Let's try it..
                // This works but only on transparency
                // Need to find the right Bitmap and IplImage matching types

				/*
				 * bitmap.copyPixelsFromBuffer(yuvIplimage.getByteBuffer());
				 * //bitmap.setPixel(10,10,Color.MAGENTA);
				 *
				 * canvas = new Canvas(bitmap); Paint paint = new Paint();
				 * paint.setColor(Color.GREEN); float leftx = 20; float topy =
				 * 20; float rightx = 50; float bottomy = 100; RectF rectangle =
				 * new RectF(leftx,topy,rightx,bottomy);
				 * canvas.drawRect(rectangle, paint);
				 *
				 * bitmap.copyPixelsToBuffer(yuvIplimage.getByteBuffer());
				 */
                // Log.v(LOG_TAG,"Writing Frame");

                try {

                    // Get the correct time
                    recorder.setTimestamp(videoTimestamp);

                    // Record the image into FFmpegFrameRecorder
                    recorder.record(yuvIplimage);

                } catch (FFmpegFrameRecorder.Exception e) {
                    Log.v(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}
