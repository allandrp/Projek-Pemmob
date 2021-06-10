package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNama, etNotelp, etEmail, etPassword;
    String nama, notelp, email, password;
    Button btnRegister;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDb;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get instance firebase
        fbAuth      = FirebaseAuth.getInstance();
        fbDb        = FirebaseDatabase.getInstance();
        dbReference = fbDb.getReference();

        //pair item layout dengan variabel
        etEmail     = findViewById(R.id.etRegisterEmail);
        etPassword  = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegisterRegister);

        //set button ke listener onClick
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        //pesan error if email kosong
        if(TextUtils.isEmpty(etEmail.getText().toString())){

            etEmail.setError("Email Required");
            return;

        }


        //pesan error if password kosong
        if(TextUtils.isEmpty(etPassword.getText().toString())){

            etPassword.setError("Password Required");
            return;

        }

        //memasukkan data isian ke variabel
        nama        = etNama.getText().toString().trim();
        notelp      = etNotelp.getText().toString().trim();
        email       = etEmail.getText().toString().trim();
        password    = etPassword.getText().toString().trim();

        //query untuk cek apakah data sudah ada di database
        Query checkUser = fbDb.getReference().child("users").orderByChild("email").equalTo(email);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //cek jika data telah ada
                if(snapshot.exists()){

                    Toast.makeText(RegisterActivity.this, "EMAIL TELAH TERDAFTAR", Toast.LENGTH_SHORT).show();

                }else{

                    fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                User dataUser = new User(nama, notelp, email, password);
                                dbReference.child("user").child(String.valueOf(task.getResult().getUser().getUid())).setValue(dataUser);

                                //kembali ke login
                                setResult(RESULT_OK);
                                finish();


                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}