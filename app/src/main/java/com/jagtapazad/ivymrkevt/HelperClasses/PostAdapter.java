package com.jagtapazad.ivymrkevt.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jagtapazad.ivymrkevt.R;

public class PostAdapter extends FirestoreRecyclerAdapter<LeaderClass, PostAdapter.PostViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdapter(@NonNull FirestoreRecyclerOptions<LeaderClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull LeaderClass model) {

        holder.fname.setText(model.getFname());
        holder.score.setText(model.getScore()+"");

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboarduserlist,parent,false);

        return new PostViewHolder(view);
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView fname,score;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.fbusername);
            score=itemView.findViewById(R.id.fbuserscore);
        }
    }

}
