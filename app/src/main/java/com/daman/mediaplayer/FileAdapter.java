package com.daman.mediaplayer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daman on 29-03-2017.
 */

public class FileAdapter extends ArrayAdapter<FileModel> {
    Context context;
    int resource;
    ArrayList<FileModel> fileList;


    public FileAdapter(Context context,  int resource,  ArrayList<FileModel> fileList) {
        super(context, resource, fileList);

        this.context = context;
        this.resource = resource;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        view = LayoutInflater.from(context).inflate(resource,parent,false);

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTitle = (TextView)view.findViewById(R.id.textView);

        // Read the ArrayList and get the Object
        FileModel fm = fileList.get(position);

        image.setBackgroundResource(fm.getImage());
        txtTitle.setText(fm.getName());


        return  view;
    }
}
