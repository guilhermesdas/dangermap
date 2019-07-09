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

import br.com.thundera.Model.Repository;

public class HTTPService extends AsyncTask<Void, Void, ArrayList<Repository>> {

//    private static final String ip = "192.168.43.84";
    private static final String ip = "192.168.1.103";
//    private static final String ip = "192.168.1.127";
    private String bairro_id;
    static final JSONParser parser = new JSONParser();

    @Override
    protected  ArrayList<Repository> doInBackground(Void... voids) {
        ArrayList<Repository> repositories = null;
//        StringBuilder resposta = new StringBuilder();
        StringBuffer resposta = new StringBuffer();

        try {
            URL url = new URL("http://" + this.ip + ":3000/repository/" + bairro_id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
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
            repositories = Repository.toRepositories((JSONArray) parser.parse(resposta.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return repositories;
    }

    public HTTPService(String bairro_id) {
        this.bairro_id = "newsbairro?neighborhood=" + bairro_id;
    }

    public HTTPService() {
        this.bairro_id = "";
    }


}
