package com.example.vitor.driverforme_cliente.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vitor.driverforme_cliente.fragmentos.AgendarServico;
import com.example.vitor.driverforme_cliente.fragmentos.MeusServicos;
import com.example.vitor.driverforme_cliente.fragmentos.ServicoImediato;
import com.example.vitor.driverforme_cliente.fragmentos.ServicosFinalizados;

/**
 * Created by Vitor on 04/06/2017.
 */

public class TabAdaptador extends FragmentStatePagerAdapter {
    private String[] abas = {"Meus Serviços", "Agendar", "Solicitar Imediato", "Finalizados"};

    public TabAdaptador(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MeusServicos();
                break;
            case 1:
                fragment = new AgendarServico();
                break;
            case 2:
                fragment = new ServicoImediato();
                break;
            case 3:
                fragment = new ServicosFinalizados();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return abas.length;
    }

    public CharSequence getPageTitle(int position) {

        return abas[position];
    }
}
