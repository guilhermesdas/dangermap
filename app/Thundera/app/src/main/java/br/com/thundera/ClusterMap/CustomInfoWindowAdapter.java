package br.com.thundera.ClusterMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import br.com.thundera.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window_map,null);
    }

    private void rendowWindowText(Marker marker, View view) throws NullPointerException{

        String title = marker.getTitle();
        Log.e("[DEBUG]", title);

        TextView tvTitle = (TextView) view.findViewById(R.id.title_custom_view);

        if (title!=null && !title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet_custom_view);

        if (!snippet.equals("")){
            tvSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        try {
            rendowWindowText(marker,mWindow);
            return mWindow;
        }catch (NullPointerException ex){
            ex.getMessage();
            return null;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        try {
            rendowWindowText(marker,mWindow);
            return mWindow;
        }catch (NullPointerException ex){
            ex.getMessage();
            return null;
        }
     }
}
