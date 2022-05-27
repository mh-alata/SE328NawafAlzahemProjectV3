package com.example.se328nawafalzahemproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Firebase extends AppCompatActivity {
    EditText Firstname;
    EditText Lastname;
    EditText Phone;
    EditText Email;
    EditText Uni_ID;
    Button Insert,Update,Delete,search,InsertFromFireBase;

    RequestQueue rq;
    User user;
    FirebaseDatabase database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Students");

        rq= Volley.newRequestQueue(this);
        rq.add(weatherHelper.weather(this));

        Firstname=(EditText)findViewById(R.id.EDTfirstName);
        Lastname=(EditText)findViewById(R.id.EDTlastName);
        Phone=(EditText)findViewById(R.id.EDTphoneNumber);
        Email=(EditText)findViewById(R.id.EDTemailAddress);
        Uni_ID=(EditText)findViewById(R.id.EDT_UNIid);

        Insert=(Button)findViewById(R.id.InsertButton);
        Update=(Button)findViewById(R.id.UpdateButton);
        Delete=(Button)findViewById(R.id.DeleteButton);
        search=(Button)findViewById(R.id.Search);
        InsertFromFireBase=(Button)findViewById(R.id.InsertFromFireBase);

        user = new User();

        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = Firstname.getText().toString();
                String lname = Lastname.getText().toString();
                String phone = Phone.getText().toString();
                String email = Email.getText().toString();
                String uid =Uni_ID.getText().toString();

                if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || uid.isEmpty()){
                    Toast.makeText(Firebase.this,"All fields are required.",Toast.LENGTH_SHORT).show();
                    return; }

                insertUser(fname,email,lname,phone,Integer.valueOf(uid));
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid=Uni_ID.getText()+"";
                if (uid.isEmpty()){
                    Toast.makeText(Firebase.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }

                delete(Integer.valueOf(uid));
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = Firstname.getText().toString();
                String lname = Lastname.getText().toString();
                String phone = Phone.getText().toString();
                String email = Email.getText().toString();
                String uid =Uni_ID.getText().toString();

                HashMap<String,Object> myMap = new HashMap<>();

                if (uid.isEmpty()){
                    Toast.makeText(Firebase.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fname.isEmpty()){
                    myMap.put("firstName",fname);
                }
                if (!lname.isEmpty()){
                    myMap.put("lastName",lname);
                }
                if (!phone.isEmpty()){
                    myMap.put("phoneNumber",phone);
                }
                if (!email.isEmpty()){
                    myMap.put("emailAddress",email);
                }
                if (!uid.isEmpty()){
                    myMap.put("userId",Integer.valueOf(uid));
                }
                update(Integer.valueOf(uid),myMap);
                }


        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Firebase.this,FirebaseList.class));
            }
        });

    }

    private void insertUser(String fName,String email,String lName,String phone,int userId){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){

                    if (child.child("userId").getValue(Integer.class)==userId){

                        Toast.makeText(Firebase.this,"A record with UID "+userId+" was found.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int counterx = (int)snapshot.getChildrenCount();

                while(snapshot.hasChild(counterx+"")){
                    counterx++;
                }

                DatabaseReference insertRef = ref.child(counterx+"");

                insertRef.child("emailAddress").setValue(email);
                insertRef.child("firstName").setValue(fName);
                insertRef.child("lastName").setValue(lName);
                insertRef.child("phoneNumber").setValue(phone);
                insertRef.child("userId").setValue(userId);
                Toast.makeText(Firebase.this,"Data inserted successfully.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void delete(int uid){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Firebase.this,"Record deleted successfully.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Firebase.this,"Error Detected: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                        return;
                    }
                }
                Toast.makeText(Firebase.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void update(int uid, Map<String,Object> keyValMap){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){

                        ref.child(child.getKey()).updateChildren(keyValMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Firebase.this,"Record updated successfully.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Firebase.this,"Error Detected: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toast.makeText(Firebase.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}