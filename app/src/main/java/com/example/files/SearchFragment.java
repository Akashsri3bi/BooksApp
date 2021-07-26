package com.example.files;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<File> Searchlist ;
    RecyclerView recyclerView ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SearchFragment(List<File> Searchlist){
        this.Searchlist = Searchlist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search , container, false);
        recyclerView = view.findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ReadingListAdapter adapter = new ReadingListAdapter(Searchlist);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setListeners(new ReadingListAdapter.Listeners() {
            @Override
            public void oneClick(int position){
                Intent intent = new Intent(getContext() , OpenBook.class);
                intent.putExtra("Path", Searchlist.get(position).toURI().toString());
                startActivity(intent);
            }
        });
        return view;
    }
}