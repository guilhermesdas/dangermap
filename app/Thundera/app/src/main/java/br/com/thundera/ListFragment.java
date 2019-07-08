package br.com.thundera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import br.com.thundera.Model.Neighborhood;
import br.com.thundera.Service.HTTPServiceBairros;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ListView listView;

    SearchView searchView;
    ArrayAdapter<String> adapter;
    Vector<String> data = new Vector<String>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Neighborhood> retorno = null;

        HTTPServiceBairros service = new HTTPServiceBairros();
        try {
            retorno = service.execute().get();
//            Log.i("[DEBUG]", retorno.toString());

            Collections.sort(retorno);

            for (int i=0; i<retorno.size(); i++){
                data.add(retorno.get(i).getName());
            }
//            LatLng local = new LatLng(Double.parseDouble(retorno.get(0).getNeighborhood().getLat()), Double.parseDouble(retorno.get(0).getNeighborhood().getLng()));
//            mMap.addMarker(new MarkerOptions().position(local).title(retorno.get(0).getNeighborhood().getName()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity()).setMessage("Não foi possível conectar com o servidor.");
            dlgAlert.setTitle("Erro de Conexão");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.idListView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);


        final ArrayList<Neighborhood> finalRetorno = retorno;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                Toast.makeText(getContext(), finalRetorno.get(position).getName(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(),LinksBairroActivity.class);
                intent.putExtra("bairro_id", finalRetorno.get(position).get_id());
                intent.putExtra("bairro_nome",finalRetorno.get(position).getName());
                startActivity(intent);

//                Intent intent = new Intent(getContext(), BairroItemActivity.class);
//                intent.putExtra("bairro_id", finalRetorno.get(position).get_id());
//                startActivity(intent);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
