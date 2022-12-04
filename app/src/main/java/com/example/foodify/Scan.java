package com.example.foodify;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.foodify.databinding.ActivityHomeBinding;
import com.example.foodify.databinding.ActivityMainBinding;
import com.example.foodify.databinding.FragmentScanInfoBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Scan extends Fragment {
    JsonResponse jsonResponse;
    String upc;
    Gson gson = new Gson();
    int minteger = 0;
    Button add;
    Button subtract;
    private RecyclerView recyclerview;
    private ArrayList<FoodItem> inventoryArrayList;

    Button addToInventory;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scan();
        add = view.findViewById(R.id.add);
        add.setOnClickListener(v ->{
            increaseInteger();
        });

        subtract = view.findViewById(R.id.subtract);
        subtract.setOnClickListener(v ->{
            decreaseInteger();
        });

        addToInventory = view.findViewById(R.id.add_to_inventory);
        addToInventory.setOnClickListener(v->{
            saveUPCToDatabase(upc, minteger);
        });
    }

    private void saveUPCToDatabase(String upc, int total){

    }

    void scan(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(BarcodeScannerActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            upc = result.getContents();
            sendUserToBarCodeInfo(upc);
        }
    });

    public void increaseInteger() {
        minteger = minteger + 1;
        display(minteger);
    }

    public void decreaseInteger() {
        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) getView().findViewById(
                R.id.total);
        displayInteger.setText("" + number);
    }

    private void sendUserToBarCodeInfo(String upc){
        TextView foodName = getView().findViewById(R.id.foodTitle);
        TextView foodUpc = getView().findViewById(R.id.foodUpc);
        TextView info = getView().findViewById(R.id.info);
        ImageView imageView = getView().findViewById(R.id.imageView);

        String Key;
        Key = "55d01a0c91msh1a5d4e55f6cf63cp174b8bjsn419908648873"; // FIX

        OkHttpClient client = new OkHttpClient();
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/products/upc/"+upc;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", Key)
                .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();

                    GsonBuilder builder = new GsonBuilder();
                    builder.serializeNulls();
                    builder.setPrettyPrinting();
                    Gson gson = builder.create();

                    jsonResponse =gson.fromJson(myResponse, JsonResponse.class);

                    getActivity().runOnUiThread(() ->{
                        foodName.setText(jsonResponse.getTitle() + " id: " + jsonResponse.getId());
                        foodUpc.setText(upc);
                        info.setText(jsonResponse.toString());
                        String url = jsonResponse.getImages().get(2).toString();
                        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
                        Glide.with(getActivity()).load(url).into(imageView);

                    });
                }
            }

        });
    }

}

