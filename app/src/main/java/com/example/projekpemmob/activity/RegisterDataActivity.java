package com.example.projekpemmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterDataActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDataRegister;
    private EditText etRegisterName;
    private EditText etRegisterPhoneNumber;
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = fbDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);

        btnDataRegister = findViewById(R.id.btnDataRegister);
        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterPhoneNumber = findViewById(R.id.etRegisterPhoneNumber);

        btnDataRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnDataRegister.getId()) {
            String name = etRegisterName.getText().toString();
            String phoneNumber = etRegisterPhoneNumber.getText().toString();
            String uidUser = mAuth.getCurrentUser().getUid();

            dbReference.child("user").child(uidUser).child("name").setValue(name);
            dbReference.child("user").child(uidUser).child("phone").setValue(phoneNumber);

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }
}