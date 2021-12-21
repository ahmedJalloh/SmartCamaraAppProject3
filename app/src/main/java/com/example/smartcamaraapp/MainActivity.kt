package com.example.smartcamaraapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Add a button that starts a camara
        findViewById<Button>(R.id.button).setOnClickListener {

            //Create an intent that launches the camara and take a pic
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val tView = findViewById<TextView>(R.id.labels)

            //Grab bitmap from image that was taken in camara
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //Set bitmap as imageview image
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)

            //Prepare bitmap for ML Kit API's
            val imageForMlKit = InputImage.fromBitmap(imageBitmap, 0)

            //Utilize ImageLabeler API
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)


            labeler.process(imageForMlKit)

                .addOnSuccessListener { labels ->
                    Log.i("Ahmed", "Successfully processed image")
                    for (label in labels) {
                        //What was detected in the image
                        val text = label.text
                        //The confidence score of what was detected
                        val confidence = label.confidence

                        val index = label.index

                        tView.text = text + " detected " + " with confidence: " + "%" + confidence;

                        //Log to logcat
                        Log.i("Ahmed", "detected:" + text + " with confidence: " + confidence)

                    }

                }
                .addOnFailureListener { e ->
                    Log.i("Ahmed", "Error processing image")

                }

        }
    }

}