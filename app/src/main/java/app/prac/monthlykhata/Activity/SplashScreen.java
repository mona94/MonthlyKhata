package app.prac.monthlykhata.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import app.prac.monthlykhata.MonthlyKhataApp;
import app.prac.monthlykhata.R;


public class SplashScreen extends AppCompatActivity {

    private String TAG = "Firebase Code";
    private long SPLASH_TIME_OUT = 3000;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (MonthlyKhataApp.getFireBaseAuth().getCurrentUser() == null) {
                    i = new Intent(SplashScreen.this, PhoneAuth.class);
                } else {
                    i = new Intent(SplashScreen.this, MainActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
