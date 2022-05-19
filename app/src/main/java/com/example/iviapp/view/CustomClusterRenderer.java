package com.example.iviapp.view;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by: Anton Shkurenko (tonyshkurenko)
 * Project: ClusterManagerDemo
 * Date: 6/7/16
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<TotemItem> {

    private final IconGenerator mClusterIconGenerator;
    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<TotemItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(TotemItem item,
                                               MarkerOptions markerOptions) {

        mClusterIconGenerator.setBackground(null);
        View view = new TotemMarker(mContext, item.getImageUrl());
        mClusterIconGenerator.setContentView(view);

        final BitmapDescriptor markerDescriptor =
                BitmapDescriptorFactory.fromBitmap(
                        mClusterIconGenerator.makeIcon()
                );

        markerOptions.icon(markerDescriptor).snippet(item.getTotemTitle());
    }

}