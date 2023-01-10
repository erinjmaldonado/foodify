package com.example.foodify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Scan extends Fragment {
    private JsonResponse jsonResponse;
    private String upc;
    private int minteger = 0;
    private Button add;
    private Button subtract;
    private Button addToInventory;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson;
    String myResponse;

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


    }

    private void saveUPCToDatabase(String title, String upc, int total, String response, String uri){
        FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listIdRef = rootRef.child(user.getUid()).child("foodItem");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String foodItem = ds.getValue(String.class);
                    list.add(foodItem);
                }
                //Do what you need to do with your list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };

        listIdRef.addListenerForSingleValueEvent(valueEventListener);
        Map<String, Object> map = new HashMap<>();
        HashMap<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("title", title);
        itemInfo.put("total", total);
        itemInfo.put("response", response);
        itemInfo.put("uri", uri);
        map.put(upc, itemInfo);
        rootRef.child(user.getUid()).child("foodList").updateChildren(map);
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
        TextView htmlContent = getView().findViewById(R.id.htmlText);
        final String[] uri = new String[1];

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
                if(response.isSuccessful()) {
                    myResponse = response.body().string();

                    String jsonString = myResponse;
                    JSONObject jsonResult = null;
                    try {
                        jsonResult = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String title = null;
                    JSONArray badgesArray = new JSONArray();
                    JSONArray imagesArray = new JSONArray();
                    JSONArray nutritionArray = new JSONArray();
                    JSONObject nutrition = null;

                    try {
                        nutrition = jsonResult.getJSONObject("nutrition");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String[] badges = new String[0];
                    String[] images = new String[0];
                    String[] nutrients = new String[0];

                    try {
                        title = (String) jsonResult.get("title");
                        badgesArray = (JSONArray) jsonResult.get("badges");
                        imagesArray = (JSONArray) jsonResult.get("images");
                        nutritionArray = nutrition.getJSONArray("nutrients");

                        List<String> badgesList = new ArrayList<String>();
                        List<String> imagesList = new ArrayList<String>();
                        List<String> nutritionList = new ArrayList<String>();

                        for (int i = 0; i < badgesArray.length(); i++) {
                            badgesList.add(badgesArray.getString(i));
                        }
                        for (int i = 0; i < imagesArray.length(); i++) {
                            imagesList.add(imagesArray.getString(i));
                        }
                        for (int i = 0; i < nutritionArray.length(); i++) {
                            nutritionList.add(nutritionArray.getString(i));
                        }

                        int badgeSize = badgesList.size();
                        int imageSize = imagesList.size();
                        int nutritionSize = nutritionList.size();

                        badges = badgesList.toArray(new String[badgeSize]);
                        images = imagesList.toArray(new String[imageSize]);
                        nutrients = nutritionList.toArray(new String[nutritionSize]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (getActivity() != null) {
                        //call the ui thread
                        String finalTitle = title;
                        String[] finalBadges = badges;
                        String[] finalImages = images;
                        String[] finalNutrients = nutrients;

                        ((Activity) requireContext()).runOnUiThread(() -> {
                            htmlContent.setText(Arrays.toString(finalNutrients));
                            foodName.setText(finalTitle);
                            foodUpc.setText(upc);
                            info.setText(Arrays.toString(finalBadges));
                            uri[0] = finalImages[2];
                            ImageView imageView1 = (ImageView) getView().findViewById(R.id.imageView);
                            Glide.with(getActivity()).load(uri[0]).into(imageView1);
                        });

                        addToInventory = getView().findViewById(R.id.add_to_inventory);
                        addToInventory.setOnClickListener(view -> {
                            saveUPCToDatabase(finalTitle, upc, minteger, Arrays.toString(finalBadges), uri[0]);
                            Toast.makeText(getActivity(), "item added", Toast.LENGTH_SHORT).show();
                        });

                    }
                }
            }

        });
    }

}

