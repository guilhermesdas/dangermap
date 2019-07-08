package br.com.thundera.Service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.thundera.Model.Neighborhood;

public class HTTPServiceBairros extends AsyncTask<Void, Void, ArrayList<Neighborhood>> {

//    private static final String ip = "192.168.43.84";
    private static final String ip = "192.168.43.10";
//    private static final String ip = "192.168.1.127";
    static final JSONParser parser = new JSONParser();

    @Override
    protected  ArrayList<Neighborhood> doInBackground(Void... voids) {
        ArrayList<Neighborhood> neighborhood = null;
        StringBuffer resposta = new StringBuffer();
        try {
            URL url = new URL("http://"+this.ip+":3000/neighborhood");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.setConnectTimeout(5000);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                resposta.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("[DEBUG]", resposta.toString());

        try {
            neighborhood = Neighborhood.toNeighborhoods((JSONArray) parser.parse(resposta.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return neighborhood;
    }


    public HTTPServiceBairros() {
    }


}
