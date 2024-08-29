package com.xpcell;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.xpcell.databinding.ActivityMainBinding;

import java.util.List;

import Adapter.RepuestoAdapter;
import Api.ApiInterface;
import Model.Repuesto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private
    RepuestoAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Repuesto>> call = apiInterface.getRepuestos(null, null);
        call.enqueue(new Callback<List<Repuesto>>() {
            @Override
            public void onResponse(Call<List<Repuesto>> call, Response<List<Repuesto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Repuesto> repuestos = response.body();
                    adapter = new RepuestoAdapter(repuestos);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Repuesto>> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_price_ascending:
                    // Handle "Menor a mayor"
                    break;
                case R.id.nav_price_descending:
                    // Handle "Mayor a menor"
                    break;
                case R.id.nav_brand_samsung:
                    // Handle "Samsung"
                    break;
                case R.id.nav_brand_apple:
                    // Handle "Apple"
                    break;
                case R.id.nav_brand_huawei:
                    // Handle "Huawei"
                    break;
                // Maneja otros ítems del menú
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    public void openDrawerLayout(View view) {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        // Mostrar/ocultar RecyclerView según el fragmento
        if (fragment instanceof HomeFragment) {
            showRecyclerView();
        } else {
            hideRecyclerView();
        }
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }
}


