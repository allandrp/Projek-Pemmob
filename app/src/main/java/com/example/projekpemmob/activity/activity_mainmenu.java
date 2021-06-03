package com.example.projekpemmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projekpemmob.R;
import com.google.firebase.auth.FirebaseAuth;

public class activity_mainmenu extends AppCompatActivity implements View.OnClickListener {


    Button btnLogout;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();

        btnLogout = findViewById(R.id.btnFoodList);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, activity_FoodList.class);
        startActivity(intent);

    }


}