package com.csi.kushagra.csipro;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class MapsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        CosawaariMapFragment fFreagment = new CosawaariMapFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container,fFreagment).commit();
    }



}
