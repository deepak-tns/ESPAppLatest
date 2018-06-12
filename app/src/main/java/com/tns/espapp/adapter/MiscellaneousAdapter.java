package com.tns.espapp.adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.database.MiscellaneousData;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by TNS on 12-Oct-17.
 */

public class MiscellaneousAdapter extends RecyclerView.Adapter<MiscellaneousAdapter.MovieViewHolder> {

    private List<MiscellaneousData> miscellaneouslist;
    private MovieViewHolder holder;
    File fileWithinMyDir;

    private int rowLayout;
    private Context context;
    private  int lastPosition;
    private final OnItemClickListener listener;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout miscellaneousAlllistLayout;
        TextView date;
        TextView message;

        ImageView btn_download;

        public MovieViewHolder(View v) {
            super(v);
       //  miscellaneousAlllistLayout = (LinearLayout) v.findViewById(R.id.miscellaneousAlllist_layout);
            date = (TextView) v.findViewById(R.id.date);
            message = (TextView) v.findViewById(R.id.message);
            btn_download =(ImageView) v.findViewById(R.id.image_download);
        }

        public void bind(final MiscellaneousData miscellaneousAlllist, final OnItemClickListener listener, final int pos) {
         /*   movieTitle.setText(miscellaneousAlllist.getTitle());
            data.setText(miscellaneousAlllist.getReleaseDate());
            movieDescription.setText(miscellaneousAlllist.getOverview());
            rating.setText(miscellaneousAlllist.getVoteAverage().toString());*/

        /*    itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    listener.onItemClick(miscellaneousAlllist,pos);

                }

            });*/

        }
    }

    public MiscellaneousAdapter(List<MiscellaneousData> miscellaneouslist, int rowLayout, Context context,  OnItemClickListener listener) {

        this.miscellaneouslist = miscellaneouslist;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MiscellaneousAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        //Animation scaleUp = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        // view.startAnimation(scaleUp);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        this.holder= holder;
       // holder.bind(miscellaneouslist.get(position),listener,position);

        /*if(position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_up);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
        if(position == 0 || position==1) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_out_up);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
*/

         MiscellaneousData data = miscellaneouslist.get(position);
        final String spil2[]= data.getFilePath().split("/");

         holder.date.setText(data.getDate());
         holder.message.setText( data.getFilePath());

        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MiscellaneousData datas = miscellaneouslist.get(position);
                String downloads = AppConstraint.MISCELLANOUSDETAILSDOWNLOADS+datas.getFilePath();
                //Log.v("image",spil2[1]);

                final String spil2s[]= datas.getFilePath().split("/");
           new DownloadFileFromURL().execute(downloads,spil2s[1]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return miscellaneouslist.size();
    }

    public static interface OnItemClickListener
    {

        void onItemClick(MiscellaneousData item, int pos);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        ImageView my_image;
        String fileName;

        // Progress dialog type (0 - for Horizontal progress bar)
        public static final int progress_bar_type = 0;
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

            /*    File mydir; //Creating an internal dir;
                mydir = context.getDir("mydir", Context.MODE_PRIVATE);
                */

                File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                if (!docDir.exists()) {
                    if (!docDir.mkdirs()) {
                        return null;
                    }
                }

                 fileName = "tns_"+ f_url[1];
                String pdfFile = docDir + "/" + fileName;
                fileWithinMyDir = new File(pdfFile); //Getting a file within the dir.
                FileOutputStream out = new FileOutputStream(fileWithinMyDir);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    out.write(data, 0, count);
                }

                // flushing output
                out.flush();

                // closing streams
                out.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
           pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            pDialog.dismiss();
           // Bitmap bm = BitmapFactory.decodeFile(fileWithinMyDir.getPath());
try {
            File url = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName);
            if (url.exists()) {
                Uri uri = Uri.fromFile(url);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                } else if (url.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "application/x-wav");
                } else if (url.toString().contains(".rtf")) {
                    // RTF file
                    intent.setDataAndType(uri, "application/rtf");
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (url.toString().contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(uri, "image/gif");
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                    // JPG file
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (url.toString().contains(".txt")) {
                    // Text file
                    intent.setDataAndType(uri, "text/plain");
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                        url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                    // Video files
                    intent.setDataAndType(uri, "video/*");
                } else {
                    intent.setDataAndType(uri, "*/*");
                }

                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent intents = Intent.createChooser(intent, "Open File");

                context.startActivity(intents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    }



}