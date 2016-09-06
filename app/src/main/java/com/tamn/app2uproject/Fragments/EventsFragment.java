package com.tamn.app2uproject.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tamn.app2uproject.Constants;
import com.tamn.app2uproject.ItemActivity;
import com.tamn.app2uproject.Model.EventItem;
import com.tamn.app2uproject.PictureHelper;
import com.tamn.app2uproject.R;
import com.tamn.app2uproject.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    RecyclerView rvEvents;
    View inflateView;
    LinearLayoutManager linearLayoutManager;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_events, container, false);
        rvEvents = (RecyclerView) inflateView.findViewById(R.id.rvEvents);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.events));

        initRecycle();
        return inflateView;
    }


    private void initRecycle() {
        //Retrieve an instance of your database using getInstance()
        // and reference the location you want to write to.
        //in our case is EVENTS
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(Constants.EVENTS);

        //Layout for recycle
        linearLayoutManager = new LinearLayoutManager(getContext());
        //reverse the order
        linearLayoutManager.setReverseLayout(true);
        rvEvents.setLayoutManager(linearLayoutManager);

        //Adapter
                                // model     //ViewHolder
        FirebaseRecyclerAdapter<EventItem, ItemsViewHolder> adapter = new FirebaseRecyclerAdapter<EventItem, ItemsViewHolder>(
                EventItem.class, //our model
                R.layout.recycle_event_item, //layout
                ItemsViewHolder.class, //ViewHolder
                ref //query , reference

        ) {
            @Override
            protected void populateViewHolder(final ItemsViewHolder viewHolder, EventItem model, final int position) {

                viewHolder.tvItemTitle.setText(model.getTitle());
                viewHolder.tvItemContent.setText(model.getContent());
                viewHolder.tvEventDate.setText(model.getEventUploadTime());

                Picasso.with(getActivity()).load(model.getUrl()).into(viewHolder.ivItemImage);

                viewHolder.rlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // get the push id of the user
                        DatabaseReference item = getRef(position);
                        String key = item.getKey();

                        Intent moveToItemActivity = new Intent(getContext(),ItemActivity.class);
                        moveToItemActivity.putExtra(Constants.ITEM_KEY, key);
                        moveToItemActivity.putExtra(Constants.EVENT_TITLE,viewHolder.tvItemTitle.getText());
                        moveToItemActivity.putExtra(Constants.EVENT_CONTENT,viewHolder.tvItemContent.getText());

                        //Singleton
                        PictureHelper.getInstance().setDrawable(viewHolder.ivItemImage.getDrawable());
                        startActivity(moveToItemActivity);

                    }
                });
                // delete item on long click //todo: remove or set it to admin only
                viewHolder.tvItemContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        DatabaseReference item =  getRef(position);
                        item.setValue(null);

                        return true;
                    }
                });
            }
        };
        //divider
        rvEvents.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        rvEvents.setAdapter(adapter);
    }

    //findViewById - provide a direct reference to the layout in the recycleView
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemContent;
        TextView tvItemTitle;
        TextView tvEventDate;
        ImageView ivItemImage;
        RelativeLayout rlItem;

        public ItemsViewHolder(View itemView) {
            super(itemView);

            tvItemContent = (TextView) itemView.findViewById(R.id.tvItemContent);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
            ivItemImage = (ImageView) itemView.findViewById(R.id.ivItemImage);
            tvEventDate = (TextView) itemView.findViewById(R.id.tvEventDate);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
        }
    }

}
