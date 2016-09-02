package com.tamn.app2uproject.Adapter;

//*
// * Created by Tamn on 31/08/2016.

/*
public class RecyclerAdapterFB extends FirebaseRecyclerAdapter<EventItem, RecyclerAdapterFB.ItemsViewHolder> {
    public RecyclerAdapterFB(Class modelClass, int modelLayout, Class viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

    }


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference dbRef = database.getReference(Constants.GIVE_TAKE);

    public RecyclerAdapterFB(Class modelClass, int modelLayout, Class viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        modelClass = EventItem.class; //our model
        modelLayout = R.layout.recycle_event_item;
        viewHolderClass = ItemsViewHolder.class;
        ref = dbRef;

    }

    @Override
    protected void populateViewHolder(final ItemsViewHolder viewHolder, EventItem model, final int position) {

        viewHolder.tvItemTitle.setText(model.getTitle());
        viewHolder.tvItemContent.setText(model.getContent());
        viewHolder.tvEventDate.setText(model.getEventUploadTime());

        Picasso.with().load(model.getUrl()).into(viewHolder.ivItemImage);

        viewHolder.tvItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the push id of the user
                DatabaseReference item = getRef(position);
                String key = item.getKey();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //todo: fix this when no user signin
                // String email = user.getEmail();

                Intent moveToItemActivity = new Intent(getContext(),ItemActivity.class);
                moveToItemActivity.putExtra(Constants.ITEM_KEY, key);
                moveToItemActivity.putExtra(Constants.EVENT_TITLE,viewHolder.tvItemTitle.getText());
                moveToItemActivity.putExtra(Constants.EVENT_CONTENT,viewHolder.tvItemContent.getText());

                //Singleton
                PictureHelper.getInstance().setDrawable(viewHolder.ivItemImage.getDrawable());
                startActivity(moveToItemActivity);

            }
        });
        // delete item on long click
        viewHolder.tvItemContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DatabaseReference item =  getRef(position);
                item.setValue(null);

                return true;
            }
        });
    }

    //findViewById - provide a direct reference to the layout in the recycleView
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemContent;
        TextView tvItemTitle;
        TextView tvEventDate;
        ImageView ivItemImage;

        public ItemsViewHolder(View itemView) {
            super(itemView);

            tvItemContent = (TextView) itemView.findViewById(R.id.tvItemContent);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
            ivItemImage = (ImageView) itemView.findViewById(R.id.ivItemImage);
            tvEventDate = (TextView) itemView.findViewById(R.id.tvEventDate);
        }
    }
}
*/