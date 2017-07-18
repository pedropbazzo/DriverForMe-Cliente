package com.example.vitor.driverforme_cliente.fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vitor.driverforme_cliente.R;
import com.example.vitor.driverforme_cliente.adaptadores.ServicoAdaptador;
import com.example.vitor.driverforme_cliente.entidades.Servico;
import com.example.vitor.driverforme_cliente.estaticos.ClienteEstatico;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusServicos extends Fragment {

    private ListView listView;
    private ServicoAdaptador adapter;
    private ArrayList<Servico> servicosAgendados;
    private ArrayList<Servico> servicosImediatos;
    private ArrayList<Servico> servicos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerServicosAgendados;
    private ValueEventListener valueEventListenerServicosImediatos;
    private ClienteEstatico ce;
    public MeusServicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_servicos, container, false);

        servicosAgendados = new ArrayList<>();
        servicosImediatos = new ArrayList<>();
        servicos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_servicos);
        ce = new ClienteEstatico();
        firebase = FirebaseEstatico.getFirebase();


        adapter = new ServicoAdaptador(getActivity(), servicos);
        listView.setAdapter(adapter);
        final Query qrServicosAgendados = firebase.child("servicosAgendadosAbertos").orderByChild("cliente").equalTo(ce.getCliente().getEmail());
        final Query qrServicosImediatos = firebase.child("servicosImediatosAbertos").orderByChild("cliente").equalTo(ce.getCliente().getEmail());

        valueEventListenerServicosAgendados = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.removeAll(servicosAgendados);
                servicosAgendados.clear();
                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    Log.i("Vamos ver", servico.toString());
                    servicosAgendados.add( servico );
                }
                servicos.addAll(servicosAgendados);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicosAgendados.addValueEventListener( valueEventListenerServicosAgendados );

        valueEventListenerServicosImediatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.removeAll(servicosImediatos);
                servicosImediatos.clear();

                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    Log.i("Vamos ver", servico.toString());
                    servicosImediatos.add( servico );
                }
                servicos.addAll(servicosImediatos);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicosImediatos.addValueEventListener( valueEventListenerServicosImediatos );


        return view;
    }

}
