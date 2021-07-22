package mb.ganesh.gilgalapplock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    final String mKEY = "BIO_INFO";
    final int mMODE = MODE_PRIVATE;
    boolean isEnabled ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences(mKEY , mMODE);
        isEnabled = sp.getBoolean(mKEY , false);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEnabled){      // fingerprint enabled
                    startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                    finish();
                }else {         // fingerprint disabled
                    startActivity(new Intent(SplashActivity.this , MainActivity.class));
                    finish();
                }
            }
        },1234);
    }
}




