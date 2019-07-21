package ru.lazybones.jkh.jkhapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OrderInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static DatabaseReference mydatabase;
    private FirebaseStorage storage;
    private StorageReference usersref;
    private StorageReference picref;
    private int numberph;
    private String numberphoto;
    private String objectuid;
    private String orderid;

    private  String key;
    String galleryimageurl;

    private static final String TAG="ex" ;
    private static final String TAGd="dialog" ;
    private ProgressBar progressBar;
    private HashMap<Integer,String> galery;
    private boolean picsload;
    private LinearLayout galerylayout;
    private GaleryDialog dlggalery;
    private TextView info;

    private  int buttoncolor;
    private boolean lock;
    private TextView lockmess;
    private TextView typePriority;
    private int idTypePrior = 0;
    private TextView typeWorks;
    private int idTypeWorks = 0;
    private ImageView addimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String status= Current.preOrder.getStatus();

        storage = FirebaseStorage.getInstance();
        usersref = storage.getReference();


        objectuid= Current.preOrder.getObjectid();
        numberph=1;
        numberphoto=numberph+"";
        orderid=Current.preOrder.getId();

        mydatabase= FirebaseDatabase.getInstance().getReference();

        addimage= new ImageView(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        key=Current.preOrder.getUserid();

        info = (TextView) findViewById(R.id.infoet);

        galerylayout = (LinearLayout) findViewById(R.id.galerylayout);
        typePriority=(TextView) findViewById(R.id.priorititv);
        typeWorks=(TextView) findViewById(R.id.typeworktv);

        mydatabase.child("objects").child(objectuid).child("galeries").child(key).child(orderid).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                galery= new HashMap<Integer, String>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        String element = snapshot1.getValue().toString();
                        Integer el = Integer.parseInt(snapshot1.getKey());
                        if (element!= null)

                            galery.put(el, element);


                    }
                }
                progressBar.setVisibility(View.GONE);
                refreshgalery();
                picsload=true;

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });


        if (status.equals("назначен исполнитель")) {
            mydatabase.child("preorders").child(objectuid).child(orderid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Current.order = dataSnapshot.getValue(Order.class);
                    orderupdate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            mydatabase.child("preorders").child(objectuid).child(orderid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Current.preOrder = dataSnapshot.getValue(PreOrder.class);
                    preorderupdate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void preorderupdate() {
        String[] typesworks = getResources().getStringArray(R.array.type_works);
        String[] priority = getResources().getStringArray(R.array.type_prior);
        typePriority.setText(priority[Current.preOrder.getPriority()]);
        typeWorks.setText(typesworks[Current.preOrder.getTipe()]);
        info.setText(Current.preOrder.getInfo());





    }

    private void orderupdate() {
        String[] typesworks = getResources().getStringArray(R.array.type_works);
        String[] priority = getResources().getStringArray(R.array.type_prior);
        typePriority.setText(priority[Current.order.getPriority()]);
        typeWorks.setText(typesworks[Current.order.getTipe()]);
        info.setText(Current.order.getInfo());






    }

    private void refreshgalery() {
        int dip = 110;
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,  getResources().getDisplayMetrics());
        dip= (int)px;
        int md = 15;
        float mpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, md,  getResources().getDisplayMetrics());
        md = (int)mpx;


        LinearLayout.LayoutParams lParams;
        lParams = new LinearLayout.LayoutParams(dip, dip, 1);
        lParams.setMargins(md,0,md,0);

        int n =galerylayout.getChildCount();
        galerylayout.removeAllViews();


        numberph=0;
        for (Map.Entry<Integer,String> mp: galery.entrySet()) {
            if (mp.getKey() > numberph) numberph =mp.getKey();
            final ImageView pic = new ImageView(this);


            Picasso.get().load(mp.getValue())
                    .error(R.drawable.loginim)
                    .placeholder(R.drawable.loginim)
                    .resize(250, 250)
                    .centerCrop()
                    .into(pic);
            galerylayout.addView(pic, lParams);
            pic.setTag( mp.getKey());

            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Current.uidnumber=(Integer) v.getTag();
                    Current.url=galery.get(Current.uidnumber);
                    dlggalery = new GaleryDialog();

                    dlggalery.show(getSupportFragmentManager(), "gallery");

                }
            });

        }
        numberph++;
        numberphoto=numberph+"";


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
            Intent intent = new Intent(this, MainDrawActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this, MyordersActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_payment) {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
