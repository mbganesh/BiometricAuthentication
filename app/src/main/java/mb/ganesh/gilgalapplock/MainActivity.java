package mb.ganesh.gilgalapplock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class MainActivity extends AppCompatActivity {

    SwitchCompat switchCompat;

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    final String mKEY = "BIO_INFO";
    final int mMODE = MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchCompat = findViewById(R.id.switchId);

        switchCompat.setChecked(updatePref());

        executor = ContextCompat.getMainExecutor(MainActivity.this);

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("GigGal AppLock")
                .setSubtitle("Login with FingerPrint")
                .setNegativeButtonText("Cancel")
                .build();

        checkSysReq();

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked()) {
                    getFingerPrint();
                    biometricPrompt.authenticate(promptInfo);
                }else {
                    switchCompat.setChecked(false);
                    saveData(mKEY , false);
                }
            }
        });
    }

    private void checkSysReq() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                switchCompat.setEnabled(false);
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                switchCompat.setEnabled(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                switchCompat.setEnabled(false);
                break;
        }
    }

    private void saveData(String key, boolean value) {
        sp = getSharedPreferences(key , mMODE);
        editor = sp.edit();
        editor.putBoolean(key , value);
        editor.apply();
    }

    private boolean updatePref() {
        sp = getSharedPreferences(mKEY , mMODE);
        return sp.getBoolean(mKEY ,switchCompat.isChecked());
    }

    private void getFingerPrint() {

        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("InBio", "Error");
                switchCompat.setChecked(false);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                switchCompat.setChecked(true);
                saveData(mKEY , true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e("InBio", "Failed");
                switchCompat.setChecked(false);
            }
        });
    }

}