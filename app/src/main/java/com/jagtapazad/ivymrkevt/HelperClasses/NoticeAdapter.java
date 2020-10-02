package com.jagtapazad.ivymrkevt.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt2;
import com.jagtapazad.ivymrkevt.R;

import java.io.InputStream;

public class NoticeAdapter extends FirestoreRecyclerAdapter<NoticeClass, NoticeAdapter.NoticeViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoticeAdapter(@NonNull FirestoreRecyclerOptions<NoticeClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoticeViewHolder holder, int position, @NonNull NoticeClass model) {

        holder.fname.setText(model.getNotice());
        holder.score.setText(model.getNumber()+"");
        if(model.getImgURL() != ""){

            new TreasureHunt2.DownloadImageTask((ImageView) holder.itemView.findViewById(R.id.noticeIMG))
                    .execute(model.imgURL);

        }

    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list,parent,false);

        return new NoticeViewHolder(view);
    }


    class NoticeViewHolder extends RecyclerView.ViewHolder{

        TextView fname,score;
        ImageView imgimg;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.fbusername);
            score=itemView.findViewById(R.id.fbuserscore);
            imgimg=itemView.findViewById(R.id.noticeIMG);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
