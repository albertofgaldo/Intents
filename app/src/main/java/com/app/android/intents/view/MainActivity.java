package com.app.android.intents.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.android.intents.R;
import com.app.android.intents.application.TimePickerFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static ImageView imagePhoto;
    Button takePicture, selectFile, goWeb, goPhone, programAlarm, setAlarm, openText;
    EditText textWeb, textPhone, textTimeHour, textTimeMin, textText;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePhoto = (ImageView)findViewById(R.id.imagePhoto);

        takePicture = (Button)findViewById(R.id.buttonPicture);
        selectFile = (Button)findViewById(R.id.buttonFileSelect);
        goWeb = (Button)findViewById(R.id.buttonGo1);
        goPhone = (Button)findViewById(R.id.buttonGo2);
        programAlarm = (Button)findViewById(R.id.buttonProgram);
        setAlarm = (Button)findViewById(R.id.setAlarmButton);
        openText = (Button)findViewById(R.id.buttonOpen);

        textWeb = (EditText)findViewById(R.id.textViewWeb);
        textPhone = (EditText)findViewById(R.id.textViewPhone);
        textTimeHour = (EditText)findViewById(R.id.textViewHour);
        textTimeMin = (EditText)findViewById(R.id.textViewMin);
        textText = (EditText)findViewById(R.id.textViewText);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAddPic();
            }
        });

        goWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb(textWeb.getText().toString());
            }
        });

        goPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(textPhone.getText().toString());
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        programAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textTimeHour.getText().toString().isEmpty() || textTimeMin.getText().toString().isEmpty()){
                    showToast("Invalid alarm time");
                }else{
                    createAlarm("Nueva alarma",Integer.parseInt(textTimeHour.getText().toString()),Integer.parseInt(textTimeMin.getText().toString()));
                }
            }
        });

        openText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textText.getText().toString().isEmpty()){
                    Intent textActivity = new Intent(MainActivity.this, textActivity.class);
                    textActivity.putExtra("text", textText.getText().toString());
                    startActivity(textActivity);
                }else{
                    showToast("Inset a text");
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textTimeHour.setText(Integer.toString(hourOfDay));
        textTimeMin.setText(Integer.toString(minute));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imagePhoto.setImageBitmap(image);
        }else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imagePhoto.setImageURI(imageUri);
        }
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void galleryAddPic() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openWeb(String url){
        if(!TextUtils.isEmpty(url)) {
            if(isUrl(url)){
                Intent i = new Intent(Intent.ACTION_VIEW);
                startActivity(i);
            }else{
                showToast("Invalid website");
            }
        }else {
            showToast("Enter a website");
        }
    }

    private void callNumber(String number){
        if(!TextUtils.isEmpty(number)) {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }else {
            showToast("Enter a phone number");
        }
    }

    public boolean isUrl(String s) {
        String regex = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";

        try {
            Pattern patt = Pattern.compile(regex);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }

    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showToast(String text){
        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // imagePhoto.setImageBitmap(image);
    }

}
