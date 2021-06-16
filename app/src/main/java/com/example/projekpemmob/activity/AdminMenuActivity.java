package com.example.projekpemmob.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFoodManagement, btnWaitingOrders, btnFinishedOrders, btnLogout;
    private CardView cvOpen;
    private TextView tvOpen;

    //FIREBASE AUTH
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        btnFoodManagement = findViewById(R.id.btnFoodManagement);
        btnWaitingOrders = findViewById(R.id.btnWaitingOrders);
        btnFinishedOrders = findViewById(R.id.btnFinishedOrders);
        btnLogout = findViewById(R.id.btnLogout);

        btnFoodManagement.setOnClickListener(this);
        btnWaitingOrders.setOnClickListener(this);
        btnFinishedOrders.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == btnFoodManagement.getId()) {
            startActivity(AdminFoodListActivity.class);
        } else if (id == btnWaitingOrders.getId()) {
            startActivity(AdminWaitingOrderActivity.class);
        } else if (id == btnFinishedOrders.getId()) {
            startActivity(AdminFinishedOrderActivity.class);
        } else if (id == btnLogout.getId()) {
            onClickLogoutButton();
        }
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void onClickLogoutButton() {
        Toast.makeText(this, "Logged out of admin.", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
    }
}