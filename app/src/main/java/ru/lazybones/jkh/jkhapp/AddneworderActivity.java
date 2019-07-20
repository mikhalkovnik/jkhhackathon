package ru.lazybones.jkh.jkhapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.Menu;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddneworderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    FirebaseUser user;
    private ImageView imageLogin;
    private TextView userName;
    private final static int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE =1;
    private DatabaseReference myRef;
    private static DatabaseReference mydatabase;
    private FirebaseStorage storage;
    private StorageReference usersref;
    private StorageReference picref;
    static final int GALLERY_REQUEST = 1;
    private  String key;
    private ImageView addimage;
    String galleryimageurl;
    String objectuid;
    String numberphoto;
    private static final String TAG="ex" ;
    private static final String TAGd="dialog" ;
    private ProgressBar progressBar;
    private HashMap<Integer,String> galery;
    private boolean picsload;
    private int numberph;
    private LinearLayout galerylayout;
    private GaleryDialog dlggalery;
    private EditText info;

    private  int buttoncolor;
    private boolean lock;
    private TextView lockmess;
    private Spinner typePriority;
    private int idTypePrior = 0;
    private Spinner typeWorks;
    private int idTypeWorks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addneworder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                saveworkerbd(view);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();


        storage = FirebaseStorage.getInstance();
        usersref = storage.getReference();
        picsload=false;

        objectuid= "12345544545";
        numberph=1;
        numberphoto=numberph+"";

        mydatabase= FirebaseDatabase.getInstance().getReference();


        addimage= new ImageView(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        key=Constants.user.getUserid();

        info = (EditText) findViewById(R.id.infoet);
        picsload=true;


        ImageView addphoto = (ImageView) findViewById(R.id.addphotoim);
        galerylayout = (LinearLayout) findViewById(R.id.galerylayout);
        typePriority=(Spinner) findViewById(R.id.prioritispiner);
        typeWorks=(Spinner) findViewById(R.id.typeworksapinner);


        typePriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idTypePrior = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(VehicleAddActivity.this, "Не выбран тип ТС",Toast.LENGTH_SHORT).show();
            }
        });

        typeWorks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idTypeWorks = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(VehicleAddActivity.this, "Не выбран тип ТС",Toast.LENGTH_SHORT).show();
            }
        });


        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!picsload)  {
                    Toast.makeText(AddneworderActivity.this, "Дождитесь синхронизации с базой данных",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int permissionStatus = ContextCompat.checkSelfPermission(AddneworderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    showimagechoser();
                }
                else {
                    ActivityCompat.requestPermissions(AddneworderActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
                }

            }
        });

        mydatabase.child("objects").child(objectuid).child("galeries").child(key).addValueEventListener(new ValueEventListener(){
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
        galerylayout.removeViews(1,n-1);


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

                    dlggalery.show(getSupportFragmentManager(), v.getTag().toString());

                }
            });



        }
        numberph++;
        numberphoto=numberph+"";


    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showimagechoser();
                }
                break;
        }
    }

    private void showimagechoser() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            // case FILE_REQUEST:
            //Uri selectedFileUri = imageReturnedIntent.getData();
            //uploadfile(selectedFileUri);
            //  break;

            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {

                    Uri selectedImageUri = imageReturnedIntent.getData();



                    Picasso.get()
                            .load(selectedImageUri)
                            .error(R.drawable.loginim)
                            .resize(600, 600)
                            .centerCrop()
                            .into(addimage, new Callback() {
                                @Override
                                public void onSuccess() {


                                    uploadpic(key);




                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(AddneworderActivity.this, "ошибка.изображение не загруженно",
                                            Toast.LENGTH_SHORT).show();


                                }


                            });




                }
        }
    }

    private void uploadpic(String key) {
        picsload=false;


        picref= usersref.child("galery").child(objectuid).child(key).child(numberphoto+".jpg");

        // Get the data from an ImageView as bytes
        addimage.setDrawingCacheEnabled(true);
        addimage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) addimage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = picref.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return picref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    getimageURL(task.getResult().toString());




                } else {


                    Toast.makeText(AddneworderActivity.this, "ошибка! Изображение  не обновленно!",
                            Toast.LENGTH_SHORT).show();

                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void getimageURL (String url)  {

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("objects").child(objectuid).child("galeries").child(key).child(numberphoto).setValue(url);
        Toast.makeText(AddneworderActivity.this, "изображение обновленно",
                Toast.LENGTH_SHORT).show();


    }

    public static void deletegalery() {

//        StorageReference desertRef = FirebaseStorage.getInstance().getReference().child("galery").child(Current.curperformer.getUseruid()).child(Current.curperformer.getItemuid()+"").child(Current.uidnumber+".jpg");
//        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                mydatabase.child("users").child(Current.curperformer.getUseruid()).child("galeries").child(Current.curperformer.getItemuid()+"").child(Current.uidnumber+"").removeValue();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Uh-oh, an error occurred!
//            }
//        });
    }

    public void saveworkerbd(View view) {

        String keyuser = Constants.user.getUserid();
        String orderid = mydatabase.child("preorders").child(objectuid).push().getKey();

        String infot = info.getText().toString();

        PreOrder updatePreorder = new PreOrder(Constants.user.getUserphone(),Constants.user.getUserid());

        updatePreorder.setInfo(infot);

        if (numberph >1) {

            ArrayList<String> photourls = new ArrayList<>();

        for (Map.Entry<Integer,String> mp: galery.entrySet()) {
           photourls.add(mp.getValue());
          }

            updatePreorder.setPhotourls(photourls);
                }
        updatePreorder.setObjectid(objectuid);
        updatePreorder.setPriority(idTypePrior);
        updatePreorder.setStage("preorder");
        updatePreorder.setTipe(idTypeWorks);
        updatePreorder.setStatus("В обработке");

        //updatePerf.setLimap(latitude);
        // updatePerf.setLongmap(longitude);

        Map<String, Object> newPerformerV = updatePreorder.toMap();


        Map<String, Object> childUpdates = new HashMap<>();


        childUpdates.put("/preorders/" + objectuid +"/"+ orderid , newPerformerV);
        childUpdates.put("/users/" + keyuser + "/preorders/" + objectuid +"/"+ orderid, newPerformerV);

        progressBar.setVisibility(View.VISIBLE);

        mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                Toast.makeText(AddneworderActivity.this, " Данные успешно обновленны!",
                        Toast.LENGTH_SHORT).show();


                progressBar.setVisibility(View.GONE);


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(AddneworderActivity.this, "ошибка. ",
                                Toast.LENGTH_SHORT).show();
                    }
                });


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
        getMenuInflater().inflate(R.menu.addneworder, menu);
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
