package com.example.foodify;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class FoodItem {

    public String title;
    public String response;
    public int total = 0;
    public String uri;

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public FoodItem(String title, String response, int total, String uri) {
        this.title = title;
        this.response = response;
        this.total = total;
        this.uri = uri;
    }

    public int getTotal() {
        return total;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResponse() {
        return response;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("response", response);
        result.put("total", total);
        result.put("uri", uri);

        return result;
    }

    public String toString(){
        return title + " " + total;
    }
    // [END post_to_map]
}
// [END post_class]