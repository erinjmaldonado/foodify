package com.example.foodify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodify.databinding.ActivityHomeBinding;
import com.example.foodify.fragments.HomeFragment;
import com.example.foodify.fragments.ListFragment;
import com.example.foodify.fragments.ProfileFragment;
import com.example.foodify.fragments.RecipesFragment;

public class Home extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){
                case R.id.Home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.List:
                    replaceFragment(new ListFragment());
                    break;
                case R.id.Recipes:
                    replaceFragment(new RecipesFragment());
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void onDestroy(){
        super.onDestroy();
        binding = null;
    }


}
