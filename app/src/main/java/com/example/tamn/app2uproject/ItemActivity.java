package com.example.tamn.app2uproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamn.app2uproject.Model.CommentItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemActivity extends AppCompatActivity {

    RecyclerView rvComment;
    String userEmail;
    TextView tvEventTitle, tvEventContent;
    ImageView ivEventImage;
    EditText etComment;
    Button btnAddComment;

    String key;
    String eventTitle;
    String eventContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        initLayout();
        getEventDetails();
        initEvents();
        initRecycle();
    }

    private void initRecycle() {

        //Layout for recycle
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        //Retrieve an instance of your database using getInstance()
        // and reference the location you want to write to.
        //in our case is Comments
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(Constants.COMMENTS).child(key);

        //Adapter
        FirebaseRecyclerAdapter<CommentItem, CommentViewHolder> adapter = new FirebaseRecyclerAdapter<CommentItem, CommentViewHolder>(
                CommentItem.class, //our model
                R.layout.recycle_comment_item, //layout
                CommentViewHolder.class, //ViewHolder
                ref //query , reference

        ) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, CommentItem model, final int position) {

                viewHolder.tvUserEmail.setText(model.getEmail());
                viewHolder.tvUserComment.setText(model.getComment());
                //viewHolder.ivEventImage.se

            }
        };
        rvComment.setAdapter(adapter);
    }

    //findViewById - provide a direct reference to the layout in the recycleView
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserComment, tvUserEmail;
        //ImageView ivEventImage;

        public CommentViewHolder(View itemView) {
            super(itemView);

            tvUserComment = (TextView) itemView.findViewById(R.id.tvUserComment);
            tvUserEmail = (TextView) itemView.findViewById(R.id.tvUserEmail);
            //ivEventImage = (ImageView) itemView.findViewById(R.id.ivEventImage);
        }
    }

    private void sendDataToDB() {
        String comment = etComment.getText().toString();

        // create an object from the model
        CommentItem commentItem = new CommentItem(comment, userEmail);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(Constants.COMMENTS).child(key);


        ref.push().setValue(commentItem).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ItemActivity.this, "Added in success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ItemActivity.this, "failed:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // clear text
        etComment.setText("");
    }

    private void initLayout() {
        tvEventTitle = (TextView) findViewById(R.id.tvEventTitle);
        tvEventContent = (TextView) findViewById(R.id.tvEventContent);
        etComment = (EditText) findViewById(R.id.etComment);
        btnAddComment = (Button) findViewById(R.id.btnAddComment);
        rvComment = (RecyclerView) findViewById(R.id.rvComment);
        ivEventImage = (ImageView) findViewById(R.id.ivEventImage);
    }

    private void initEvents() {
        //Title
        tvEventTitle.setText(eventTitle);
        //Content
        tvEventContent.setText(eventContent);
        //Image
        ivEventImage.setImageDrawable(PictureHelper.getInstance().getDrawable());

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToDB();
            }
        });
    }

    private void getEventDetails() {

        Intent commentIntent = getIntent();
        // getting the key from main intent
        key = commentIntent.getStringExtra(Constants.ITEM_KEY);
        userEmail = commentIntent.getStringExtra(Constants.USER_EMAIL);
        eventTitle = commentIntent.getStringExtra(Constants.EVENT_TITLE);
        eventContent = commentIntent.getStringExtra(Constants.EVENT_CONTENT);

    }

}
