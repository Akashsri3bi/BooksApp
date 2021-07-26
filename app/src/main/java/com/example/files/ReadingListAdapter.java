package com.example.files;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadingListAdapter extends RecyclerView.Adapter<ReadingListAdapter.Viewholder> {

    private List<File> files ;
    private Listeners listeners ;

    interface Listeners{
        void oneClick(int position) ;
    }

    public void setListeners(Listeners listeners){
        this.listeners =  listeners;
    }

    public ReadingListAdapter(List<File> files){
        this.files = files ;
    }

    @NonNull
    @Override
    public ReadingListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycler_items ,parent , false) ;
        return new ReadingListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingListAdapter.Viewholder holder, final int position) {
        File pdffile = files.get(position);
        String book = pdffile.getName();
        Bitmap bitmap = pdf_to_Bitmap(pdffile) ;
        holder.image.setImageBitmap(bitmap);
        holder.title.setText(book);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listeners!=null) {
                    listeners.oneClick(position);
                }
            }
        });
        /**
        holder.Star.setOnClickListener();
         Code for star / Favorites .

         **/
    }

    public void Remove(File file){
        files.remove(file) ;
        notifyDataSetChanged();
    }

    private Bitmap pdf_to_Bitmap(File pdfFile){
        Bitmap bitmap = null ;
        try{
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile , ParcelFileDescriptor.MODE_READ_ONLY));
            final int page_count = renderer.getPageCount() ;
            if(page_count>0){
                PdfRenderer.Page page = renderer.openPage(0) ;
                int width = page.getWidth();
                int height = page.getHeight();
                bitmap = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888);

                page.render(bitmap , null , null , PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {

        if(files!=null){
            return files.size() ;
        }
        return 0 ;
    }

    class Viewholder extends RecyclerView.ViewHolder{
        TextView title ;
        ImageView image ;
        ImageView Star  ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            image = itemView.findViewById(R.id.book_image);
            Star = itemView.findViewById(R.id.star);
        }
    }
}
