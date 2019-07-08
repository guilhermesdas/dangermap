package br.com.thundera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import br.com.thundera.Model.Repository;
import br.com.thundera.Service.HTTPService;

public class LinksBairroActivity extends AppCompatActivity {

    ListView listView;

    ArrayAdapter<String> adapter;
    Vector<String> data = new Vector<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_bairro);
        ArrayList<Repository> retorno = null;

        Intent intent = getIntent();
        String bairro_id = (String) intent.getSerializableExtra("bairro_id");
        String bairro_nome = (String) intent.getSerializableExtra("bairro_nome");
//        Log.i("[DEBUG]", bairro_id);

        listView = (ListView) findViewById(R.id.idListViewBairros);

        TextView tvBairro = (TextView) findViewById(R.id.textViewBairro);
        tvBairro.setText("Notícias relacionadas ao bairro "+bairro_nome);


        HTTPService service = new HTTPService(bairro_id);
        try {
            retorno = service.execute().get();
//            Log.i("[DEBUG]", retorno.toString());

            if(retorno.size()==0){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this).setMessage("O DangerMap ainda não encontrou notícias relacionadas a este bairro.");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }else{
                for (int i=0; i<retorno.size(); i++)
                    data.add(retorno.get(i).getBrief());
            }


//            LatLng local = new LatLng(Double.parseDouble(retorno.get(0).getNeighborhood().getLat()), Double.parseDouble(retorno.get(0).getNeighborhood().getLng()));
//            mMap.addMarker(new MarkerOptions().position(local).title(retorno.get(0).getNeighborhood().getName()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this).setMessage("Não foi possível conectar com o servidor.");
            dlgAlert.setTitle("Erro de Conexão");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }


        // Inflate the layout for this fragment

        if (data == null){
            Log.e("[DEBUG]","Erro de ponteiro nulo");
        }else {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
//            Log.e("[DEBUG]",String.valueOf(adapter.getCount()));

            listView.setAdapter(adapter);
        }

        final ArrayList<Repository> finalRetorno = retorno;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = (String) finalRetorno.get(position).getLink().getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

}
