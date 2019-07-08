package br.com.thundera.ClusterMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private  String bairro_id;
    private String link;


    public ClusterMarker(double lat, double lng, String bairro_id, String title, String snippet, String link) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.link = link;
        this.bairro_id = bairro_id;
    }

    public ClusterMarker(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }

    public ClusterMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getBairro_id() {
        return bairro_id;
    }

    public String getLink() {
        return link;
    }
}
