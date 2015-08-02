package com.wlz.imageupload;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<Image> arrayListUri;
    ImageListAdapter adapter;
    ListView imageListView;
    FloatingActionButton pickButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayListUri = new ArrayList<>();
        adapter = new ImageListAdapter(getApplicationContext(),arrayListUri);
        imageListView = (ListView) findViewById(R.id.image_listview);
        pickButton = (FloatingActionButton) findViewById(R.id.fab);
        imageListView.setAdapter(adapter);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_Picker_Dialog();
            }
        });

    }

    public void Image_Picker_Dialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Pick image to upload");
        myAlertDialog.setMessage("Select Mode");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplicationContext(), AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 9-arrayListUri.size());
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent pictureActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pictureActionIntent, 2);

            }
        });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != Activity.RESULT_OK) return;
        switch(requestCode) {
            case Constants.REQUEST_CODE:
                ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                arrayListUri.addAll(images);
                break;
            case 2:
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Image camera_image = new Image(0,"camera_image",getImageUri(getApplicationContext(),imageBitmap).toString(),false);
                arrayListUri.add(camera_image);
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            for (int i = 0; i <arrayListUri.size() ; i++) {
                Log.e("URI",arrayListUri.get(i).path);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
