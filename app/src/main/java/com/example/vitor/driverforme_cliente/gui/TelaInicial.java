package com.example.vitor.driverforme_cliente.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.vitor.driverforme_cliente.R;
import com.example.vitor.driverforme_cliente.adaptadores.TabAdaptador;
import com.example.vitor.driverforme_cliente.estaticos.ClienteEstatico;

public class TelaInicial extends AppCompatActivity {
    private Toolbar toolbar;
    private ClienteEstatico clienteEstatico;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DriverForMe");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //Configurar adapter
        TabAdaptador tabAdapter = new TabAdaptador( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:

                return true;
            case R.id.item_editar:

                return true;
            case R.id.item_excluir:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
