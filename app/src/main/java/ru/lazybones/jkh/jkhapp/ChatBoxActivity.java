package ru.lazybones.jkh.jkhapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ChatBoxActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton iconsmile, iconmicro,iconplus,arrowicon,pointsicon;
    private EditText textfield;
    private Button greenbtn;
    private ImageView imageLogin;
    private TextView name, online;
    private DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    private LinearLayout mlayout;
    private String id;
    private ArrayList<Message> messages;
    private ProgressBar progressBar;
    private static final String TAG = "RecyclerViewExample";
    private SimpleDateFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        mydatabase= FirebaseDatabase.getInstance().getReference();


        Buffer.curuser = new User();
        Buffer.curuser.setUserid(Current.preOrder.getUserid());
        Buffer.curuser.setUserphone(Current.preOrder.getUserphone());


        df= new SimpleDateFormat("HH:mm");


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mlayout = (LinearLayout) findViewById(R.id.messageboxla);
        iconsmile = (ImageButton) findViewById(R.id.iconsmile);
        iconmicro = (ImageButton) findViewById(R.id.iconmicro);
        iconplus = (ImageButton) findViewById(R.id.iconplus);
        arrowicon = (ImageButton) findViewById(R.id.arrowicon);
        pointsicon =(ImageButton) findViewById(R.id.pointsicon);

        textfield =(EditText) findViewById(R.id.textfield);

        greenbtn = (Button) findViewById(R.id.greenbtn);
        imageLogin = (ImageView) findViewById(R.id.thumbnail);
        name = (TextView) findViewById(R.id.title);
        online= (TextView) findViewById(R.id.phone);

        arrowicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





//        View.OnClickListener listentoprofile = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChatBoxActivity.this, ProfileAboutActivity.class);
//
//                startActivity(intent);
//                finish();
//            }
//        };

        String sr = Buffer.curuser.getUserid();

        Picasso.get().load(Buffer.photourlpart1+sr+Buffer.photourlpart2)
                .error(R.drawable.placeholderim)
                .placeholder(R.drawable.placeholderim)
                .transform( new CropCircleTransformation())
                .into(imageLogin);

        name.setText(Buffer.curuser.getUserlogin());


//        imageLogin.setOnClickListener(listentoprofile);
//        name.setOnClickListener(listentoprofile);
//        online.setOnClickListener(listentoprofile);


        iconsmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iconmicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        iconplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        pointsicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        greenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();

            }
        });

        id = Current.preOrder.getId();

        messages = new ArrayList<>();

        mydatabase.child("messages").child(id).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                messages = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 :
                            snapshot.getChildren()) {

                        Message element = snapshot1.getValue(Message.class);
                        if (element!= null)

                            messages.add(element);


                    }
                }
                updateUI();
                progressBar.setVisibility(View.GONE);

            }

            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
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

    private void updateUI() {
        mlayout.removeAllViews();
        for (Message mes: messages) {

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);


            TextView tv = new TextView(this );
            tv.setText(mes.getText());
            TextView time = new TextView(this);
            time.setText(df.format(new Date(mes.getTime())));


            LinearLayout.LayoutParams lParamstv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams lParamstime = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            lParamstv.gravity= Gravity.LEFT;
            lParamstime.gravity=Gravity.RIGHT;


            int dip = 270;
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,  getResources().getDisplayMetrics());
            dip= (int)px;

            int pad =18;
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pad,  getResources().getDisplayMetrics());
            pad= (int)px;

            lParamstv.setMargins(pad,pad,pad,0);
            lParamstime.setMargins(0,0,pad/4,pad/4);

            layout.addView(tv,lParamstv);
            layout.addView(time,lParamstime);




            // try {
            //     Typeface typeface = getResources().getFont(R.font.montserratregular);
            //    tv.setTypeface(typeface);
            //  } catch (Exception e) {}


            tv.setTextColor(ContextCompat.getColor(this,R.color.verydarkgreytext));
            tv.setTextSize(15);
            time.setTextSize(10);



            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            mlayout.addView(layout,lParams);


            if (Constants.user.getUserid().equals(mes.getAftorid())) {
                lParams.gravity= Gravity.RIGHT;
                lParams.setMargins(4*pad,pad,pad,0);
                layout.setLayoutParams(lParams);
                layout.setBackgroundResource(R.drawable.roundedlight);

            }
            else {
                lParams.setMargins(pad,pad,4*pad,0);
                lParams.gravity= Gravity.START;
                layout.setLayoutParams(lParams);
                layout.setBackgroundResource(R.drawable.roundedlightwhite);
            }
            tv.setFocusableInTouchMode(true);
            tv.requestFocus();
            textfield.setFocusableInTouchMode(true);
            textfield.requestFocus();
        }

    }

    private void sendmessage() {
        Message message = new Message(Constants.user.getUserid() ,textfield.getText().toString());
        Map<String, Object> messagemap = message.toMap();

        Chat chat = new Chat(Constants.user.getUserid(), Buffer.curuser.getUserid(),Buffer.curuser.getUserlogin(), message);
        Chat chat1 =new Chat(Buffer.curuser.getUserid() ,Constants.user.getUserid(),Constants.user.getUserlogin(), message);
        Map<String, Object> chatmap = chat.toMap();
        Map<String, Object> chat1map = chat1.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/messages/" + id+"/"+message.getId(), messagemap);
        childUpdates.put("/chats/" +Constants.user.getUserid()+"/"+ id, chatmap);
        childUpdates.put("/chats/" +Buffer.curuser.getUserid()+"/"+ id, chat1map);

        mydatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                textfield.setText("");
                // Write was successful!
                // ...

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(ChatBoxActivity.this, "ошибка.Сообщение не отправленно",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
