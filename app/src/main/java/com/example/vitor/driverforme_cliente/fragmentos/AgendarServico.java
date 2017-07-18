package com.example.vitor.driverforme_cliente.fragmentos;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vitor.driverforme_cliente.estaticos.ClienteEstatico;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.example.vitor.driverforme_cliente.R;
import com.example.vitor.driverforme_cliente.adaptadores.TabAdaptador;
import com.example.vitor.driverforme_cliente.entidades.Servico;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_cliente.gui.TelaInicial;
import com.example.vitor.driverforme_cliente.logica.ClienteLogica;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.SphericalUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendarServico extends Fragment {

    private EditText fdData, fdHorario, fdObservacoes;
    private Button btOrigem, btDestino;
    private TextView fdRelatorio;
    private Button btCalcular, btConfirmar;
    private Servico servico = new Servico();
    private DatabaseReference servicosAbertos = FirebaseEstatico.getFirebase().child("servicosAgendadosAbertos");
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private final static int PLACE_PICKER_REQUEST_ORIGEM = 1;
    private final static int PLACE_PICKER_REQUEST_DESTINO = 2;
    LatLng coordenadasOrigem, coordenadasDestino;
    private final static LatLngBounds bounds = new LatLngBounds(new LatLng(51.5152192, -0.1321900), new LatLng(51.5166013, -0.1299262));
    //logica do cliente
    private ClienteLogica cl;
    //cliente estatico
    private ClienteEstatico ce;
    public AgendarServico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agendar_servico, container, false);

        fdData = (EditText) view.findViewById(R.id.fdData);
        fdHorario = (EditText) view.findViewById(R.id.fdHorario);
        fdObservacoes = (EditText) view.findViewById(R.id.fdObservacoes);

        btOrigem = (Button) view.findViewById(R.id.btOrigem);
        btDestino = (Button) view.findViewById(R.id.btDestino);

        fdRelatorio = (TextView) view.findViewById(R.id.fdRelatorio);

        btCalcular = (Button) view.findViewById(R.id.btCalcular);
        btConfirmar = (Button) view.findViewById(R.id.btConfirmar);

        ce = new ClienteEstatico();

        SimpleMaskFormatter mascaraData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(fdData, mascaraData);

        SimpleMaskFormatter mascaraHorario = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher maskHorario = new MaskTextWatcher(fdHorario, mascaraHorario);

        fdData.addTextChangedListener(maskData);
        fdHorario.addTextChangedListener(maskHorario);

        cl = new ClienteLogica();

        btOrigem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(bounds);
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST_ORIGEM);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.i("Erro", "Eroo no google play services repairable");
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.i("Erro", "Eroo no google play services available");
                    e.printStackTrace();
                }
            }
        });
        btDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(bounds);
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST_DESTINO);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.i("Erro", "Eroo no google play services repairable");
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.i("Erro", "Eroo no google play services available");
                    e.printStackTrace();
                }
            }
        });

        btCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cl.verificaCampo(fdData)&&cl.verificaCampo(fdHorario)&&coordenadasDestino!=null&&coordenadasOrigem!=null) {
                    servico.setData(fdData.getText().toString());
                    servico.setHorario(fdHorario.getText().toString());
                    servico.setPreco(calculaPreco());
                    servico.setTipo("Agendado");
                    fdRelatorio.setText(servico.toString());
                }
                else{
                    Toast.makeText(getContext(), "Preencha todos os campos requeridos", Toast.LENGTH_LONG).show();
                }
            }
        });
        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cl.verificaCampo(fdData)&&cl.verificaCampo(fdHorario)&&coordenadasDestino!=null&&coordenadasOrigem!=null) {
                    servico.setData(fdData.getText().toString());
                    servico.setHorario(fdHorario.getText().toString());
                    servico.setPreco(calculaPreco());
                    servico.setCliente(ce.getCliente().getEmail());
                    servico.setTipo("Agendado");
                    //salvar o serviço no banco de dados
                    servico.setId(servicosAbertos.push().getKey());
                    servicosAbertos.child(servico.getId()).setValue(servico);
                    Toast.makeText(getContext(), "Pedido realizado com sucesso", Toast.LENGTH_LONG).show();

                    fdData.setText("");
                    fdHorario.setText("");
                    btOrigem.setText("ORIGEM");
                    btDestino.setText("DESTINO");
                    coordenadasDestino = null;
                    coordenadasOrigem = null;
                    fdRelatorio.setText("");
                    fdObservacoes.setText("");
                    servico = null;
                }else{
                    Toast.makeText(getContext(), "Preencha todos os campos requeridos", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }


    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Este app requer permissão de sua localiação para funcionar corretamente", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST_ORIGEM){
            if (resultCode == RESULT_OK){
                Place placeOrigem = PlacePicker.getPlace(getActivity(), data);
                btOrigem.setText("Orig: "+placeOrigem.getName().toString());
                servico.setOrigem(placeOrigem.getAddress().toString());
                coordenadasOrigem = placeOrigem.getLatLng();
                if (placeOrigem.getAttributions() == null) {
                    Log.i("no attribution", "text/html; charset=utf-8");
                } else {
                    Log.i(placeOrigem.getAttributions().toString(), "text/html; charset=utf-8");
                }
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST_DESTINO){
            if (resultCode == RESULT_OK){
                Place placeDestino = PlacePicker.getPlace(getActivity(), data);
                btDestino.setText("Des: "+placeDestino.getName().toString());
                servico.setDestino(placeDestino.getAddress().toString());
                coordenadasDestino = placeDestino.getLatLng();
                if (placeDestino.getAttributions() == null) {
                    Log.i("no attribution", "text/html; charset=utf-8");
                } else {
                    Log.i(placeDestino.getAttributions().toString(), "text/html; charset=utf-8");
                }
            }
        }
    }

    public double calculaPreco(){
        double distancia = SphericalUtil.computeDistanceBetween(coordenadasDestino, coordenadasOrigem);
        distancia  = distancia/850;
        Log.i("Distancia é: ", String.valueOf(distancia/1000));
        return distancia*4;
    }
}
