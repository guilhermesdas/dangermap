package br.com.thundera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.thundera.ClusterMap.ClusterMarker;
import br.com.thundera.ClusterMap.CustomInfoWindowAdapter;
import br.com.thundera.Model.Repository;
import br.com.thundera.Service.HTTPService;
import java.util.Random;


public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback,
                            GoogleMap.OnMapClickListener, LocationListener, ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker> {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final String TAG = "MapsFragment";

    private String url_link_marker = "";

    // Declare a variable for the cluster manager.
    private ClusterManager<ClusterMarker> mClusterManager;


    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ClusterMarker>(getContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    }

    private void addItems(ArrayList<Repository> repositories) {
        Random random = new Random();
        for (int i=0; i<repositories.size(); i++){

            mClusterManager.addItem(new ClusterMarker( (Double.parseDouble(repositories.get(i).getNeighborhood().getLat()) + (random.nextDouble()-0.5)/120 ),
                    (Double.parseDouble(repositories.get(i).getNeighborhood().getLng()) + (random.nextDouble()-0.5)/120),
                    repositories.get(i).getNeighborhood().get_id(),
                    repositories.get(i).getNeighborhood().getName(),
                    repositories.get(i).getBrief(),
                    repositories.get(i).getLink().getLink()));

//            Log.i("[DeGUB]", repositories.get(i).getLink().getLink());
//            mMap.addMarker(new MarkerOptions().position(local).title(retorno.get(i).getNeighborhood().getName()));
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }


    @Override
    public void onResume(){
        super.onResume();
        // Ativa GPS
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100, this);
    }

    @Override
    public void onPause(){
        super.onPause();
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);


        setUpClusterer();

        // Posiciona Mapa em Manaus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-3.0636416,-59.9973157), 11));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

        // Add a marker in Sydney and move the camera

        HTTPService service = new HTTPService();

        try {
            ArrayList<Repository> retorno = service.execute().get();

//            Log.i("[DEBUG]", retorno.toString());
            addItems(retorno);

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


        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoWindowAdapter(getContext()));


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getUrl_link_marker()));
                startActivity(i);
                Log.e("[DEBUG]", "Abrindo link: "+getUrl_link_marker());
            }
        });


        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
            @Override
            public boolean onClusterItemClick(ClusterMarker clusterMarker) {
                setUrl_link_marker(clusterMarker.getLink());
                return false;
            }
        });


//        mMap.addMarker(new MarkerOptions().position(new LatLng(10,10)).title("Marcador 2"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(15,15)).title("Marcador 3"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(20,20)).title("Marcador 4"));

    }

    @Override
    public void onMapClick(LatLng latLng) {
//        Toast.makeText(getContext(), "Coordenadas: " + latLng.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getActivity(), "Posição Alterada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast.makeText(getActivity(), "O Status do Provider foi Alterada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(getActivity(), "Provider Habilitado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(getActivity(), "Provider Desabilitado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterMarker clusterMarker) {

        //Cluster item InfoWindow clicked, set title as action

        Intent intent = new Intent(getContext(),LinksBairroActivity.class);
        intent.putExtra("bairro_id", clusterMarker.getBairro_id());
        startActivity(intent);

        //You may want to do different things for each InfoWindow:
        if (clusterMarker.getTitle().equals("some title")){

            //do something specific to this InfoWindow....

        }
    }

    public String getUrl_link_marker() {
        return url_link_marker;
    }

    public void setUrl_link_marker(String url_link_marker) {
        this.url_link_marker = url_link_marker;
    }
}
