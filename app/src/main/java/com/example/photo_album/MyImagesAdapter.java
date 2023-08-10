package com.example.photo_album;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImagesHolder> {
    List<Myimages> imagesList = new ArrayList<>();
    private OnImageClickListner listner;

    public void setListner(OnImageClickListner listner) {
        this.listner = listner;
    }

    public void setImageList(List<Myimages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public interface OnImageClickListner{

        void  onImageClick(Myimages myimages);
    }

    public  Myimages getPosition(int position){
        return imagesList.get(position);
    }

    public MyImagesAdapter() {
        // Remove this line, it's redundant
        // this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public MyImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        return new MyImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImagesHolder holder, int position) {
        Myimages myimages = imagesList.get(position);
        holder.textViewTitle.setText(myimages.getImage_title());
        holder.textViewDescription.setText(myimages.getImage_description());
        //holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myimages.getImage(), 0, myimages.getImage().length));

        byte[] imageBytes = myimages.getImage();
        if (imageBytes != null && imageBytes.length>0){
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length));
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyImagesHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewDescription;

        public MyImagesHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDes);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if(listner !=null && position !=RecyclerView.NO_POSITION){
                        listner.onImageClick(imagesList.get(position));
                    }
                }
            });
        }
    }
}
