package com.example.files;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadingFragment extends Fragment {

    RecyclerView recyclerView ;
    ReadingListAdapter adapter;
    TextView texts ;
    private List<File> filelist = new ArrayList<>();
    private List<File> copyFile = new ArrayList<>() ;
    List<Integer> Positions = new ArrayList<>() ;
    private static final String PREF = "com.example.files.PREF" ;
    BooksDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_reading, container, false);

        // Inflate the layout for this fragment.

        recyclerView = view.findViewById(R.id.reading_recycler);
        texts = view.findViewById(R.id.text) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        ListDir(root);

        db = new BooksDatabase(getContext()) ;

        new GetAsyncTask().execute();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
                int position = viewHolder.getAdapterPosition() ;
                db.Delete(copyFile.get(position).getName());
                adapter.Remove(copyFile.get(position));
                adapter.notifyDataSetChanged();
                Snackbar.make(recyclerView , "Removed" , Snackbar.LENGTH_SHORT).show();
            }

        }) ;

        itemTouchHelper.attachToRecyclerView(recyclerView);
        if(adapter!=null) {
            adapter.setListeners(new ReadingListAdapter.Listeners() {
                @Override
                public void oneClick(int position) {
                    Intent intent = new Intent(getContext() , OpenBook.class);
                    intent.putExtra("Path", copyFile.get(position).toURI().toString());
                    startActivity(intent);
                }
            });
        }

        return view ;
    }

    class GetAsyncTask extends AsyncTask<Void, Void ,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = db.getData();
            Set<Integer> pos_Set = new HashSet<Integer>() ;

            while (cursor.moveToNext()) {
                int position = cursor.getInt(1);
                Positions.add(position);
                pos_Set.addAll(Positions);
            }
            for (int i : pos_Set){
                copyFile.add(filelist.get(i)) ;
            }
            Collections.reverse(copyFile);

            adapter = new ReadingListAdapter(copyFile);
            recyclerView.setAdapter(adapter);

            if (recyclerView.getAdapter().getItemCount() == 0) {
                recyclerView.setVisibility(View.GONE);
                texts.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                texts.setVisibility(View.GONE);
            }

            adapter.setListeners(new ReadingListAdapter.Listeners() {
                    @Override
                    public void oneClick(int position){
                        Intent intent = new Intent(getContext() , OpenBook.class);
                        intent.putExtra("Path", copyFile.get(position).toURI().toString());
                        startActivity(intent);
                    }
                });

            cursor.close();

            return null ;
        }
    }

    void ListDir(File f) {
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                ListDir(file);
            } else {
                if (file.getName().endsWith(".pdf")) {
                    filelist.add(file);
                }
            }
        }
    }


}