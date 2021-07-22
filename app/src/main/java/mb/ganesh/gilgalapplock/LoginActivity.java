package mb.ganesh.gilgalapplock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    ImageView fingerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fingerImage = findViewById(R.id.fingerImageId);
        fingerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        executor = ContextCompat.getMainExecutor(LoginActivity.this);
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("GigGal AppLock")
                .setSubtitle("Login with FingerPrint")
                .setNegativeButtonText("Cancel")
                .build();

        getFingerPrint();
        biometricPrompt.authenticate(promptInfo);
    }

    private void getFingerPrint() {

        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("InBio", "Error");
                biometricPrompt.authenticate(promptInfo);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e("InBio", "Failed");
                biometricPrompt.authenticate(promptInfo);
            }
        });

    }

}