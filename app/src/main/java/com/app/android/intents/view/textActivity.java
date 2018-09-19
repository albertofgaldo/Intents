package com.app.android.intents.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.app.android.intents.R;

import org.w3c.dom.Text;

import static com.app.android.intents.R.layout.activity_text;

public class textActivity extends Activity {

    TextView text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_text);

        text = (TextView) findViewById(R.id.centerText);
        text.setText(getIntent().getStringExtra("text"));
    }
}
