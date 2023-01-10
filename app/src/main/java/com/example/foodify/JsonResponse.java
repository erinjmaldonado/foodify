package com.example.foodify;

import java.net.URI;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResponse {
    @Expose
    private Integer id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    private Integer price;
    @Expose
    private Object likes;
    @Expose
    private List<String> badges = null;
    @Expose
    private List<String> importantBadges = null;
    @Expose
    private String nutritionWidget;
    @Expose
    private String servingSize;
    @Expose
    private Integer numberOfServings;
    @Expose
    private Integer spoonacularScore;
    @Expose
    private List<String> breadcrumbs = null;
    @Expose
    private String generatedText;
    @Expose
    private Object ingredientCount;
    @Expose
    private List<URI> images = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Object getLikes() {
        return likes;
    }

    public void setLikes(Object likes) {
        this.likes = likes;
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

    public List<String> getImportantBadges() {
        return importantBadges;
    }

    public void setImportantBadges(List<String> importantBadges) {
        this.importantBadges = importantBadges;
    }

    public String getNutritionWidget() {
        return nutritionWidget;
    }

    public void setNutritionWidget(String nutritionWidget) {
        this.nutritionWidget = nutritionWidget;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Integer getSpoonacularScore() {
        return spoonacularScore;
    }

    public void setSpoonacularScore(Integer spoonacularScore) {
        this.spoonacularScore = spoonacularScore;
    }

    public List<String> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<String> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    public Object getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(Object ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public List<URI> getImages() {
        return images;
    }

    public void setImages(List<URI> images) {
        this.images = images;
    }

    public String toString(){
        return badges + " ";
    }
}