package ru.lazybones.jkh.jkhapp;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.MapObject;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    private LocationManager locationManager;
    private DatabaseReference mydatabase;
    private ProgressBar progressBar;
    private LinearLayout parkmenu;
    private static final int REQUEST_CODE_SCAN = 0x0000c0de;
    private FloatingActionButton scanQrCodeButton;
    MapObject userplace;
    boolean allupdates;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ArrayList<PreOrder> preOrders;
    private RecyclerView mRecyclerView;
    private ReserveRVadapter adapter;
    private TextView preordtv;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, SignINActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Constants.user = new User();
            Constants.user.setUserid(currentUser.getUid());
            Constants.user.setUserphone(currentUser.getPhoneNumber());
            settoken();
            updatedp();

        }
    }

    private void updatedp() {
        mydatabase.child("users").child(Constants.user.getUserid()).child("preorders").child(Constants.objectid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                preOrders = new ArrayList<>();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            dataSnapshot.getChildren()) {

                        PreOrder element = snapshot1.getValue(PreOrder.class);
                        if (element!= null)

                            preOrders.add(element);


                    }

                }
                progressBar.setVisibility(View.GONE);
                Current.preOrders=preOrders;
                updatevepreorders();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void updatevepreorders() {
        if (preOrders.size()>0) preordtv.setText("Мои текущие заявки");
        else preordtv.setText("Моих заявок в работе нет");


        adapter = new ReserveRVadapter(this, preOrders);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReserveRVadapter.OnItemClickListener() {
            @Override
            public void onItemClick(PreOrder item, View v) {

                Current.preOrder=item;

//                if (v.getId()==R.id.thumbnailts) {
//                    //button vehicle
//                    if (item.getVehicleid()==null) {
//                        chooservehlt.setVisibility(View.VISIBLE);
//                    }
//
//
//                }
//                else {
//
//                    bottomSheet.show(getSupportFragmentManager(), "");
//                }



            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_draw);

        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(MainDrawActivity.this, AddneworderActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.rec_view_orders);
        LinearLayoutManager   linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        preordtv = (TextView) findViewById(R.id.ordersinfotv);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Constants.objectid = "12345544545";


    }

    private void settoken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        mydatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("nottoken").setValue(token);

                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("allnot");

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
