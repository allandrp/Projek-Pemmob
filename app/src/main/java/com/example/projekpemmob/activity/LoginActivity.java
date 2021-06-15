package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnLoginGoogle;
    private TextView tvRegister;

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDb;
    private DatabaseReference dbReference;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    int reqCode = 1;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get instance firebase
        fbAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        dbReference = fbDb.getReference();
        pb = findViewById(R.id.progressBarFoto);

        //cek apakah user masih login
        if(fbAuth.getCurrentUser() != null){
            dbReference.child("user").child(fbAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if(fbAuth.getCurrentUser().getEmail().toString().equalsIgnoreCase("admin@gmail.com")){

                        Intent intent = new Intent(LoginActivity.this, AdminMenu.class);
                        pb.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                        finish();

                    }else if (user.getName().equals("-")) {

                        Intent intentLogin = new Intent (LoginActivity.this, RegisterDataActivity.class);
                        pb.setVisibility(View.INVISIBLE);
                        startActivity(intentLogin);
                        finish();

                    }else {

                        Intent intentLogin = new Intent (LoginActivity.this, MainMenuActivity.class);
                        pb.setVisibility(View.INVISIBLE);
                        startActivity(intentLogin);
                        finish();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }else{
            pb.setVisibility(View.GONE);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //pair item layout dengan variabel
        etEmail        = findViewById(R.id.id_txEmailLogin);
        etPassword     = findViewById(R.id.id_txPwLogin);
        btnLogin       = findViewById(R.id.id_btnLoginSignIn);
        btnLoginGoogle = findViewById(R.id.id_btnLoginSignInGoogle);
        tvRegister     = findViewById(R.id.id_tvLoginRegister);

        //set button ke listener onClick
        btnLogin.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //pindah ke halaman register dengan intent
        if(v.getId() == tvRegister.getId()){

            Intent intentRegister = new Intent(this, RegisterActivity.class);
            this.startActivityForResult(intentRegister, reqCode);

        } else if(v.getId() == btnLogin.getId()){ //masuk ke halaman utama dengan firebase auth

            //memberi notifikasi jika kolom email kosong
            if(TextUtils.isEmpty(etEmail.getText().toString().trim())){

                etEmail.setError("Email Required");
                return;

            }

            //memberi notifikasi jika kolom password kosong
            if(TextUtils.isEmpty(etPassword.getText().toString().trim())){

                etPassword.setError("Password Required");
                return;

            }

            //melakukan sign in dengan firebase Auth
            fbAuth.signInWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        dbReference.child("user").child(fbAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if(fbAuth.getCurrentUser().getEmail().toString().equalsIgnoreCase("admin@gmail.com")) {
                                    Intent intent = new Intent(LoginActivity.this, AdminMenu.class);
                                    startActivity(intent);
                                    finish();
                                } else if (user.getName().equals("-")) {
                                    Intent intentLogin = new Intent (LoginActivity.this, RegisterDataActivity.class);
                                    startActivity(intentLogin);
                                    finish();
                                } else {
                                    Intent intentLogin = new Intent (LoginActivity.this, MainMenuActivity.class);
                                    startActivity(intentLogin);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    } else {

                        Toast.makeText(LoginActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } else if (v.getId() == btnLoginGoogle.getId()) {
            signIn();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fbAuth.getCurrentUser();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TEST_GOOGLE_LOGIN", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TEST_GOOGLE_LOGIN", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TEST_GOOGLE_LOGIN", "signInWithCredential:success");
                            FirebaseUser user = fbAuth.getCurrentUser();

                            dbReference.child("user").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user.getName().equals("-")) {
                                            Intent intentLogin = new Intent (LoginActivity.this, RegisterDataActivity.class);
                                            startActivity(intentLogin);
                                            finish();
                                        } else {
                                            Intent intentLogin = new Intent (LoginActivity.this, MainMenuActivity.class);
                                            startActivity(intentLogin);
                                            finish();
                                        }
                                    } catch (NullPointerException e) {
                                        User dataUser = new User("-", "-", user.getEmail(), "id10sd18d280912dhj20jdase");
                                        dbReference.child("user").child(String.valueOf(task.getResult().getUser().getUid())).setValue(dataUser);

                                        Intent intentLogin = new Intent (LoginActivity.this, RegisterDataActivity.class);
                                        startActivity(intentLogin);
                                        finish();
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TEST_GOOGLE_LOGIN", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
}