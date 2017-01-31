package com.example.arbaz.audiochat.Screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arbaz.audiochat.Global.Global;
import com.example.arbaz.audiochat.R;

/**
 * Created by arbaz on 7/10/16.
 */

public class WelcomeActivity extends Activity {
    EditText et_wc_name;
    Button btn_wc_submit;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Global.checkPermission(WelcomeActivity.this);

        et_wc_name = (EditText) findViewById(R.id.et_wc_name);
        btn_wc_submit = (Button) findViewById(R.id.btn_wc_submit);


        btn_wc_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = et_wc_name.getText().toString();
                if (!TextUtils.isEmpty(user_name)) {
                    Intent iNext = new Intent(getApplicationContext(), MainActivity.class);
                    iNext.putExtra("userName", user_name);
                    startActivity(iNext);

                    finish();
                } else {
                    et_wc_name.setError("Enter User Name");
                }


            }
        });
    }


}
