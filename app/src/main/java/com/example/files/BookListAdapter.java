package com.example.files;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.Viewholder> {

    private List<File> files ;
    private Listener listener ;

    interface Listener{
        void oneClick(int position) ;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public BookListAdapter(List<File> files){
        this.files = files ;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycler_items ,parent , false) ;
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        File pdffile = files.get(position);
        String book = pdffile.getName();
        Bitmap bitmap = pdf_to_Bitmap(pdffile) ;
        holder.image.setImageBitmap(bitmap);
        holder.title.setText(book);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.oneClick(position);
                }
            }
        });
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
        return files.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        TextView title ;
        ImageView image ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            image = itemView.findViewById(R.id.book_image);
        }
    }
}
