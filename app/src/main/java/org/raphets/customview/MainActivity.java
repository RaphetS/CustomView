package org.raphets.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        addLisetener();
    }


    private void initUI() {
        btnClock= (Button) findViewById(R.id.btn_clock);
    }

    private void addLisetener() {
        btnClock.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clock:
                startActivity(new Intent(MainActivity.this,ClockActivity.class));
                break;

            default:
                break;
        }
    }
}
