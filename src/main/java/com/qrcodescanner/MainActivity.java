package com.qrcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
    private Button scanBtn;
    private Button sendBtn;
    private  Button deleteBtn;
    String message = "Scan Values :\n";
    ArrayList<String> addArray = new ArrayList<String>();
    ArrayList<Integer> deleteArray = new ArrayList<Integer>();
    private ListView contentTxt;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> deladapter;
    ListView Listview;
    String scanContent;
    String msg = "Gokul";
    String pos;
    int position;
    SparseBooleanArray positionchecker;
    int count;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button) findViewById(R.id.scanbutton);
        sendBtn = (Button) findViewById(R.id.sendbutton);
        deleteBtn = (Button) findViewById(R.id.deletebutton);
        contentTxt = (ListView) findViewById(R.id.Listview);
        scanBtn.setOnClickListener(this);
        Listview = (ListView) findViewById(R.id.Listview);
    }

    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();


        Log.i(msg, "Entered to Camera");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result.getContents() == null || result.getContents().equals("")) {
            Toast.makeText(getBaseContext(), "Scanned Results in List", Toast.LENGTH_LONG).show();
        } else if (result.getContents() != null) {
            String scanContent = result.getContents();
            String scanFormat = result.getFormatName();
            Log.i(msg, "Barcode Readed");
            addArray.add(scanContent);
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, addArray);
            contentTxt.setAdapter(adapter);
            Log.i(msg, "barcode stored in array");

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a barcode or QRcode");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();


        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }



        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItemPositions = Listview.getCheckedItemPositions();
                int itemCount = Listview.getCount();

                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        adapter.remove(addArray.get(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
            }
        });


    }



    public void onMail(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("email"));
            for(int i=0;i<addArray.size();i++){
                message = message + (i+1) +" ) "+ addArray.get(i) + "\n";
            }
            String[] s = {"kumaraveld@kandco.in"};
            ArrayList<String> array = new ArrayList<String>();
            intent.putExtra(Intent.EXTRA_EMAIL, s);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Scan Results");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            intent.setType("message/rfc822");
            if (message == null){
                Toast.makeText(getBaseContext(),"Empty Values are did not send mail",Toast.LENGTH_LONG).show();
            }
            else{
            Intent ic = Intent.createChooser(intent, "Launch Email With");
            startActivity(ic);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error! It Seems that No E- Mail App Installed ",
                    Toast.LENGTH_SHORT).show();
        }
    }
}