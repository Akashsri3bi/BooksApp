package com.example.files;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.shockwave.pdfium.PdfPasswordException;

public class OpenBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_book);

        Intent intent = getIntent() ;
        String extra = intent.getStringExtra("Path");
        Uri uri = Uri.parse(extra) ;

        //**********************//***********************//*****************************//*************

        PDFView pdfView = findViewById(R.id.pdf) ;
        pdfView.fromUri(uri).load();
    }
}