package com.poojab26.textrecognizer;
//adb shell am start -n com.poojab26.textrecognizer/.MainActivity -e name1 bit.ly\/2WGDLFp
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.poojab26.textrecognizer.GraphicUtils.GraphicOverlay;
import com.poojab26.textrecognizer.GraphicUtils.TextGraphic;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.poojab26.textrecognizer.GraphicUtils.GraphicOverlay.Graphic;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    String l,t,t2;
    Bitmap myBitmap;
    String s1;
    int s2;

    @BindView(R.id.graphic_overlay) GraphicOverlay mGraphicOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Intent intent1 = getIntent();
        //assert "string-value".equals(intent1.getStringExtra("name1"));
        s1 = intent1.getStringExtra("name1");
        s2 = intent1.getIntExtra("name2", -1);
        //Toast toast1 = Toast.makeText(getApplicationContext(),
                //"Link:" + s1,
                //Toast.LENGTH_SHORT);
        //toast1.show();
        //s1 = "https://practice.typekit.com/social/l002-social.jpg";

        new DownloadFilesTask().execute(s1);



    }

    private void runTextRecognition(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        //
        detector.detectInImage(image).addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (blocks.size() == 0) {
            Log.d("TAG", "No text found");
            return;
        }
        //mGraphicOverlay.clear();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {

                    t = elements.get(k).getText();
                    Log.d("TAG", "" + t);
                    //System.out.println(t);
                    GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
                    mGraphicOverlay.add(textGraphic);

                }


                }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mCameraView.start();
    }

    @Override
    public void onPause() {
        //mCameraView.stop();
        super.onPause();
    }


    private class DownloadFilesTask extends AsyncTask<String,String,String> {
        protected String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                runTextRecognition(myBitmap);
                //
            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }
    }

}
