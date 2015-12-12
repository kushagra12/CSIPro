package com.csi.kushagra.cosawaaridriver;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by welcome on 11/21/2015.
 */
public class VitCampusMapFragment extends Fragment {

    PhotoViewAttacher mAttacher;
    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = (inflater.inflate(R.layout.vit_map_layout, container, false));
        img = (ImageView) v.findViewById(R.id.imageView2);
        mAttacher = new PhotoViewAttacher(img);

        return v;
    }
}
