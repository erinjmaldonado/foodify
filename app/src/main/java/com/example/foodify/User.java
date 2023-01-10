package com.example.foodify;

import java.util.HashMap;

public class User {

    public HashMap<String, HashMap<String, Object>> foodList;

    public User(HashMap<String, HashMap<String, Object>> foodList){
        this.foodList = foodList;
    }
    public HashMap<String, HashMap<String, Object>> getFoodList(){
        return foodList;
    }

    public void setFoodList(HashMap<String, HashMap<String, Object>> foodList){
        this.foodList = foodList;
    }

}
