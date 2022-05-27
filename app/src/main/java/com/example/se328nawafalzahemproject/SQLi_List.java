package com.example.se328nawafalzahemproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class SQLi_List extends AppCompatActivity {



    ArrayList<User> myUsers;
    RequestQueue rq;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqli_list);

        DBHelper dbHelper=new DBHelper(this);
        rq= Volley.newRequestQueue(this);
        rq.add(weatherHelper.weather(this));

        EditText inp_uid = findViewById(R.id.EDT_UNIid1);
        Button select = findViewById(R.id.bttn_search);
       ListView listx = findViewById(R.id.sql_list);

        myUsers=new ArrayList<>();


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c;
               String uid = inp_uid.getText().toString();


                if (uid.isEmpty()){
                    c = dbHelper.selectby_All();
                }
                else{
                    c = dbHelper.selectby_UnvID(Integer.valueOf(uid));
                }

                if (c.getCount() == 0){
                    Toast.makeText(SQLi_List.this,"No match found.",Toast.LENGTH_LONG).show();

                    myUsers.clear();
                    ((BaseAdapter)listx.getAdapter()).notifyDataSetChanged();

                    return;
                }

            //    myUsers = new ArrayList<>();

                myUsers.clear();

                do {
                    User myUser = new User();
                    myUser.setUserId( c.getInt(0));
                    myUser.setFirstName( c.getString(1));
                    myUser.setLastName( c.getString(2));
                    myUser.setPhoneNumber( c.getString(3));
                    myUser.setEmailAddress( c.getString(4));

                    myUsers.add(myUser);
                }while (c.moveToNext());



                ((BaseAdapter)listx.getAdapter()).notifyDataSetChanged();
            }
        });


        listx.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return myUsers.size();
            }

            @Override
            public Object getItem(int position) {
                return myUsers.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User x = myUsers.get(position);

                TableLayout table=(TableLayout) LayoutInflater.from(SQLi_List.this).inflate(R.layout.list_item,parent,false);

                TextView output_uid=table.findViewById(R.id.out_fb_uid);
                TextView output_name=table.findViewById(R.id.out_fb_name);
                TextView output_phone=table.findViewById(R.id.out_fb_phone);
                TextView output_email=table.findViewById(R.id.out_fb_email);

                output_uid.setText(""+x.userId);
                output_name.setText(x.firstName+" "+x.lastName);
                output_phone.setText(x.phoneNumber);
                output_email.setText(x.emailAddress);

                return table;
            }
        });

        listx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u= myUsers.get(position);

                Toast.makeText(SQLi_List.this,u.getFirstName()+" "+ u.getLastName(),Toast.LENGTH_SHORT).show();
            }
        });

        displayList();


    }

    public void displayList(){
        Cursor c;
        DBHelper dbHelper=new DBHelper(this);
        ListView listx = findViewById(R.id.sql_list);

            c = dbHelper.selectby_All();


        if (c.getCount() == 0){
            Toast.makeText(SQLi_List.this,"No match found.",Toast.LENGTH_LONG).show();

            myUsers.clear();
            ((BaseAdapter)listx.getAdapter()).notifyDataSetChanged();

            return;
        }

        //    myUsers = new ArrayList<>();

        myUsers.clear();

        do {
            User myUser = new User();
            myUser.setUserId( c.getInt(0));
            myUser.setFirstName( c.getString(1));
            myUser.setLastName( c.getString(2));
            myUser.setPhoneNumber( c.getString(3));
            myUser.setEmailAddress( c.getString(4));

            myUsers.add(myUser);
        }while (c.moveToNext());



        ((BaseAdapter)listx.getAdapter()).notifyDataSetChanged();
    }
}