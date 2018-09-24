package com.example.ademuz.exordonezwebscraping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoticiaViewHolder> {

    List<Noticias> noticias;
    Context context;

    RVAdapter(List<Noticias> noticias, Context context){
        this.noticias = noticias;
        this.context = context;
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {

        CardView cvItems;
        TextView tvTitle,tvContent,tvhora;
        ImageView ivImagen;
        RelativeLayout parentLayout;

        NoticiaViewHolder(View itemview){
            super(itemview);
            cvItems = (CardView)itemview.findViewById(R.id.cvItems);
            tvhora = (TextView)itemview.findViewById(R.id.txt_hora);
            tvTitle = (TextView)itemview.findViewById(R.id.txt_title);
            ivImagen = (ImageView)itemview.findViewById(R.id.iv_imagen);
            tvContent = (TextView)itemview.findViewById(R.id.txt_content);
            parentLayout = (RelativeLayout) itemview.findViewById(R.id.parent_layout);
        }

    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }


    @Override
        public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        NoticiaViewHolder nvh = new NoticiaViewHolder(view);
        return nvh;
        
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticiaViewHolder contactViewHolder, int i){

        String title = noticias.get(i).titulo.toUpperCase();
        contactViewHolder.tvhora.setText(noticias.get(i).hora);
        contactViewHolder.tvTitle.setText(title);
        contactViewHolder.tvContent.setText(noticias.get(i).contenido);
        final Bitmap originalBitmap = noticias.get(i).imagen;
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);

        contactViewHolder.ivImagen.setImageDrawable(roundedBitmapDrawable);
        final int position = contactViewHolder.getAdapterPosition();

        contactViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("EXTRA_POSITION", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
