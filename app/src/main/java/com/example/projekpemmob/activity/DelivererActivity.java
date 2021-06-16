package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.model.History;
import com.example.projekpemmob.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class DelivererActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    //APPLICATION
    private ImageView ivProfilePhoto;
    private TextView tvProfileName, tvFoodsList, tvFoodsPrice;
    private Button btnDelivered;
    private int totalHarga;

    //FIREBASE DATABASE
    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private MapView mapView;

    private double customerLong;
    private double customerLat;
    private String uid;
    private String hid;
    private History history;
    private User user;

    public static String EXTRA_LONG = "LONGITUDE";
    public static String EXTRA_LAT = "LATITUDE";
    public static String EXTRA_UID = "USERID";
    public static String EXTRA_HISTORY_ID = "HISTORY_ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_deliverer);

        Bundle intent = getIntent().getExtras();
        uid = intent.getString(EXTRA_UID);
        hid = intent.getString(EXTRA_HISTORY_ID);

        fbAuth          = FirebaseAuth.getInstance();
        fbDB            = FirebaseDatabase.getInstance();
        dbReference     = fbDB.getReference();

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvFoodsList = findViewById(R.id.tvFoodsList);
        tvFoodsPrice = findViewById(R.id.tvFoodPrice);
        btnDelivered = findViewById(R.id.btnDelivered);

        loadData();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void loadData() {
        getUserData();

        Query dataChart = dbReference.child("history").child(uid).child(hid);

        dataChart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    totalHarga = 0;

                    history = snapshot.getValue(History.class);
                    totalHarga = history.getTotalPrice();
                    customerLat = history.getLatitude();
                    customerLong = history.getLongitude();

                    tvFoodsPrice.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(totalHarga));

                    Query foodListQuery = dbReference.child("history").child(uid).child(hid).child("listMakanan");
                    foodListQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            String foodsList = "";

                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                FoodCart food = dataSnapshot.getValue(FoodCart.class);
                                foodsList += "(" + food.getJumlahPesan() + ")" + food.getNama() + " ";
                            }

                            tvFoodsList.setText(foodsList);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getUserData() {
        Query qImage = dbReference.child("profile image").child(uid);
        Query user = dbReference.child("user").child(uid);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tvProfileName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        qImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    FirebaseStorage fbStorage       = FirebaseStorage.getInstance();
                    StorageReference stReference    = fbStorage.getReference("user");
                    stReference = stReference.child(fbAuth.getCurrentUser().getUid()).child(snapshot.getValue().toString());

                    stReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("uri", "alamat : "+uri.toString());
                            Picasso.with(DelivererActivity.this).load(uri).into(ivProfilePhoto);
                        }
                    });
                } else {
                    ivProfilePhoto.setImageResource(R.drawable.ic_profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();

        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(customerLong, customerLat)));

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        DelivererActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(customerLat, customerLong))
                        .zoom(10)
                        .tilt(20)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void onClickDeliveredButton(View view) {
        dbReference.child("history").child(uid).child(hid).child("status").setValue("done");
        Toast.makeText(this, "Food has been delivered.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AdminMenuActivity.class);
        startActivity(intent);
    }
}