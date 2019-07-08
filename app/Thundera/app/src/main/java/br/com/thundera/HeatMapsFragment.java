package br.com.thundera;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import br.com.thundera.Model.Repository;
import br.com.thundera.Service.HTTPService;

public class HeatMapsFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final String TAG = "MapsFragment";


    /**
     * Alternative heatmap gradient (blue -> red)
     * Copied from Javascript version
     */
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };

    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);


    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

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

        // Posiciona Mapa em Manaus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-3.0636416,-59.9973157), 11));


        HTTPService service = new HTTPService();

        try {
            ArrayList<Repository> retorno = service.execute().get();
            ArrayList<LatLng> entrada = new ArrayList<LatLng>();
//            Log.i("[DEBUG]", retorno.toString());

            Random random = new Random();
            for(int i=0; i<retorno.size();i++)
                entrada.add(new LatLng((Double.parseDouble(retorno.get(i).getNeighborhood().getLat())+ (random.nextDouble()-0.5)/120),(Double.parseDouble(retorno.get(i).getNeighborhood().getLng())+ (random.nextDouble()-0.5)/120)));

            mProvider = new HeatmapTileProvider.Builder().data(entrada).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

            mProvider.setGradient(ALT_HEATMAP_GRADIENT);

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

}
