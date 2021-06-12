package com.example.projekpemmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projekpemmob.R;

public class RegisterDataActivity extends AppCompatActivity {

    private Button btnDataRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);

        btnDataRegister = findViewById(R.id.btnDataRegister);

        btnDataRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });
    }
}