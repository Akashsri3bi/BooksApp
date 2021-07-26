package com.example.files;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class BookListFragment extends Fragment {
    RecyclerView recyclerView  ;

    private List<File> filelist = new ArrayList<>() ;
    private static final String PREF = "com.example.files.PREF" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_book_list, container, false);
        recyclerView = view.findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        File root  = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        ListDir(root) ;
        return view ;
    }

    void ListDir(File f){
        File[] files = f.listFiles() ;
        for(File file : files){
            if(file.isDirectory()){
                ListDir(file);
            }else{
                if(file.getName().endsWith(".pdf")){
                    filelist.add(file);
                }
            }
        }

        BookListAdapter bookListAdapter = new BookListAdapter(filelist) ;
        recyclerView.setAdapter(bookListAdapter);

        bookListAdapter.setListener(new BookListAdapter.Listener() {
            @Override
            public void oneClick(int position) {

                //SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF , Context.MODE_PRIVATE);
                //sharedPreferences.edit().putString("Pos" , String.valueOf(position)).apply();

                BooksDatabase db = new BooksDatabase(getContext());
                db.Insert(filelist.get(position).getName() , position);

                Intent intent = new Intent(getContext() , OpenBook.class);
                intent.putExtra("Path" , filelist.get(position).toURI().toString());
                startActivity(intent);
            }
        });
    }
}