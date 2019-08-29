package github.syamildzul.mybot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    public static String accType = "";

    TextView name;
    ImageView profilePicture;
    Button signOut;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.txt_view_name);
        profilePicture = findViewById(R.id.img_view_prof_pic);
        signOut = findViewById(R.id.btn_sign_out);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            isLoggedIn = true;
            accType = "google";
        }


        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (accType == "google") {
                loadGoogleAccount();
            } else if (accType == "facebook") {

            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), accType,Toast.LENGTH_LONG).show();

        if (isLoggedIn ) {

            switch (accType) {
                case "google": loadGoogleAccount(); break;
                default: Toast.makeText(getApplicationContext(), "Failed",Toast.LENGTH_LONG).show();
                //case "facebook": break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLoggedIn = false;
        accType = "";
    }

    private void loadGoogleAccount() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            String personName = account.getDisplayName();
            Uri personPhoto = account.getPhotoUrl();

            Glide.with(this).load(String.valueOf(personPhoto)).into(profilePicture);
            name.setText(personName);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_sign_out) {
                    googleSignOut();
                }
            }
        });
    }

    private void googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed out successfully!", Toast.LENGTH_LONG);
                        isLoggedIn = false;
                        finish();
                    }
                });
    }

}
