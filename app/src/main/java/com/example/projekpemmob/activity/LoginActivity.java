package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLogin, btnLoginGoogle;
    TextView tvRegister;
    FirebaseAuth fbAuth;
    int reqCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get instance firebase
        fbAuth = FirebaseAuth.getInstance();

        //cek apakah user masih login
        if(fbAuth.getCurrentUser() != null){

            Intent intentLogin = new Intent (LoginActivity.this, MainMenuActivity.class);
            startActivity(intentLogin);
            finish();

        }

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

        }

        //masuk ke halaman utama dengan firebase auth
        else if(v.getId() == btnLogin.getId()){

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

                    if(task.isSuccessful()){

                        Intent intentLogin = new Intent (LoginActivity.this, MainMenuActivity.class);
                        startActivity(intentLogin);
                        finish();

                    }else{

                        Toast.makeText(LoginActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }
}