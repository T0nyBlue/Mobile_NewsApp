package com.example.mynews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynews.models.NewsApiResponse;
import com.example.mynews.models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{
RecyclerView recyclerView;
CustomAdapter adapter;
ProgressDialog dialog;
Button b1,b2,b3,b4,b5,b6,b7;
SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

RequestManager manager =new RequestManager(this);
manager.getNewsHeadlines(listener,"general",null);

searchView=findViewById(R.id.search_view);
searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
       dialog.setTitle("Fetching news articles of "+query);
       dialog.show();
        RequestManager manager =new RequestManager(MainActivity.this);
        manager.getNewsHeadlines(listener,"general",query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
});

dialog=new ProgressDialog(this);
dialog.setTitle("Fetching news Articles..");
dialog.show();

b1=findViewById(R.id.btn_1);
b1.setOnClickListener(this);

b2=findViewById(R.id.btn_2);
b2.setOnClickListener(this);

b3=findViewById(R.id.btn_3);
b3.setOnClickListener(this);

b4=findViewById(R.id.btn_4);
b4.setOnClickListener(this);

b5=findViewById(R.id.btn_5);
b5.setOnClickListener(this);

b6=findViewById(R.id.btn_6);
b6.setOnClickListener(this);

b7=findViewById(R.id.btn_7);
b7.setOnClickListener(this);

    }
private final OnFetchDataListener<NewsApiResponse> listener =new OnFetchDataListener<NewsApiResponse>() {
    @Override
    public void onFetchData(List<NewsHeadlines> list, String message) {
        if (list.isEmpty()) {
            Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
        } else {
            showNews(list);
            dialog.dismiss();
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(MainActivity.this, "An Error Occured!", Toast.LENGTH_SHORT).show();
    }
};

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
startActivity(new Intent(MainActivity.this,DetailsActivity.class)
.putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category =button.getText().toString();
       if(category.equals("general")) {
           dialog.setTitle("Fetching news of " + category);
       }
       else{
           dialog.setTitle("Fetching news of "+category);
           dialog.show();
       }
        RequestManager manager =new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null);
    }

    boolean isPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(isPressedOnce){
            finish();
        }
        else{
            isPressedOnce=true;
            Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> isPressedOnce=false,2000);
        }
    }
}