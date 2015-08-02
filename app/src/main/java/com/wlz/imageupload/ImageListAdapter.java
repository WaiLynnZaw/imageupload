package com.wlz.imageupload;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

/**
 * Created by WaiLynnZaw on 8/2/15.
 */
public class ImageListAdapter extends BaseAdapter {
   Context ctx;
    ArrayList<Image> arrayList;
    public ImageListAdapter(Context ctx, ArrayList<Image> arrayList){
        this.ctx = ctx;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList == null? 0:arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_listview_item,viewGroup,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Glide.with(ctx).load(arrayList.get(i).path).centerCrop().into(imageView);
        return view;
    }
}
