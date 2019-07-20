package ru.lazybones.jkh.jkhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends AppCompatActivity {

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


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference();
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


}
