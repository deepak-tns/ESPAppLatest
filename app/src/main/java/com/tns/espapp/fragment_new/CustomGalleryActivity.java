package com.tns.espapp.fragment_new;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.R;
import com.tns.espapp.database.CustomGalleryData;

import java.io.File;
import java.util.ArrayList;

public class CustomGalleryActivity extends AppCompatActivity {
    Gallery simpleGallery;
    CustomGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    // array of images
    //int[] images =
    ArrayList <CustomGalleryData> arrayList = new ArrayList<>();
    TextView tv_name;
    Button gall_iv_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gallery);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gall_iv_delete =(Button)findViewById(R.id.gall_iv_delete);
        simpleGallery = (Gallery) findViewById(R.id.simpleGallery); // get the reference of Gallery
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView); // get the reference of ImageView
        tv_name =(TextView)findViewById(R.id.tv_name);
        File path = Environment.getExternalStorageDirectory();
        File addpath = new File(path.getAbsolutePath()+"/ESP/SurveyFormFixRow/");

        try {

            File[] files = addpath.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (files[i].getAbsolutePath().contains(".jpg")) {
                    arrayList.add(new CustomGalleryData(files[i].getAbsolutePath(),files[i].getName()));
                    Log.v("getfile", files[i].getAbsolutePath().toString());
                }
            }
            if (arrayList.size() > 0){
                Bitmap myBitmap = BitmapFactory.decodeFile(arrayList.get(arrayList.size()-1).getVideo_id());
                selectedImageView.setImageBitmap(myBitmap);
                // arrayList.add("/storage/emulated/0/ESP/SurveyFormFixRow/19-03-18_11:02:45.jpg");
                customGalleryAdapter = new CustomGalleryAdapter(getApplicationContext(), arrayList); // initialize the adapter
                simpleGallery.setAdapter(customGalleryAdapter); // set the adapter
                simpleGallery.setSpacing(10);
                customGalleryAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(this,"No Image Found in Database",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("getfile",e.getMessage());
        }

        // perform setOnItemClickListener event on the Gallery
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // set the selected image in the ImageView
               // selectedImageView.setImageResource(images[position]);
                int data = (Integer) parent.getItemAtPosition(position);

                Bitmap myBitmap = BitmapFactory.decodeFile(arrayList.get(data).getVideo_id());
                Log.v("data",data+"");
                selectedImageView.setImageBitmap(myBitmap);
                tv_name.setText(arrayList.get(data).getVideo_name());

            }
        });

  /*      gall_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               File file= new File(arrayList.get(0).getVideo_id());
                if(file.exists())
                {
                    file.delete();
                   arrayList.remove(arrayList.get(0).getVideo_id());
                    customGalleryAdapter.notifyDataSetChanged();

                }
            }
        });*/
    }
    public class CustomGalleryAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<CustomGalleryData> images;

        public CustomGalleryAdapter(Context c, ArrayList<CustomGalleryData>images) {
            context = c;
            this.images = images;
        }

        // returns the number of images
        public int getCount() {
            return images.size();
        }

        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }

        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }

        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {

            // create a ImageView programmatically
            ImageView imageView = new ImageView(context);
            CustomGalleryData data = images.get(position);
            Bitmap myBitmap = BitmapFactory.decodeFile(data.getVideo_id());
            imageView.setImageBitmap(myBitmap);
           // imageView.setImageResource(images.get(position)); // set image in ImageView
            imageView.setLayoutParams(new Gallery.LayoutParams(250, 250)); // set ImageView param
            return imageView;

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
