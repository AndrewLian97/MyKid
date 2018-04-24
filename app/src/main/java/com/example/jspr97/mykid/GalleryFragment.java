package com.example.jspr97.mykid;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class GalleryFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private int activity_id;
    private ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_images, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity_id = getArguments().getInt(KidActivity.KEY_ID);
        imageView = rootView.findViewById(R.id.activityImage);

        update();
    }

    public void update() {
        UserSQL db = new UserSQL(getActivity());
        KidActivity kidActivity = db.getKidActivity(activity_id);
        String imagePath = kidActivity.getImagePath();
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
    }
}
