package org.sperixlabs.androidhotspotapi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sperixlabs.androidhotspotapi.api.WifiAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Clients extends Fragment {
    private WifiAddresses wAddr;
    private RecyclerView recyclerView;
    private List<Client> clientList = new ArrayList<>();
    private ClientsAdapter clientsAdapter;

    public Clients() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadClients(inflater, container, savedInstanceState);
            }
        });
        // Inflate the layout for this fragment
        return loadClients(inflater, container, savedInstanceState);
    }

    private View loadClients(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        View clientsView = inflater.inflate(R.layout.fragment_clients, container, false);

        recyclerView = clientsView.findViewById(R.id.recycler_view);


        wAddr = new WifiAddresses(this.getContext());
        /*Toast.makeText(getContext(),"GatWay IP:" +wAddr.getGatewayIPAddress()+"\n"+"GatWay MAC: "+wAddr.getGatWayMacAddress()+"\n"+"Device IP: "+wAddr.getDeviceIPAddress()+"\n"+"Device MAC: "+wAddr.getDeviceMacAddress(),
                Toast.LENGTH_SHORT).show();*/

        List<String> results3 = wAddr.getAllDevicesIp();


        clientsAdapter = new ClientsAdapter(clientList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(clientsAdapter);

        //populating data
        for (String result : results3) {
            /*Toast.makeText(getContext(),result,
                    Toast.LENGTH_SHORT).show();*/
            String hostname = "N/A";
            try {
                hostname = InetAddress.getByName(result).getHostName();
            } catch (UnknownHostException e) {
                // error in getting hostname
            }
            String macAddr = wAddr.getArpMacAddress(result);
            clientList.add(new Client(hostname, result, macAddr));
        }
        clientsAdapter.notifyDataSetChanged();

        return clientsView;
    }
}
