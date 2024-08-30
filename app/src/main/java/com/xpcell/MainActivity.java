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
import java.util.Collections;
import java.util.List;
import Adapter.MarcaAdapter;
import Adapter.RepuestoAdapter;
import Api.ApiInterface;
import Model.Marca;
import Model.Modelo;
import Model.Repuesto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RepuestoAdapter repuestoAdapter;
    private MarcaAdapter marcaAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<Repuesto> repuestos;
    private List<Marca> marcas;
    private Retrofit retrofit;
    private ApiInterface apiInterface;
    private boolean isShowingModels = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);

        fetchRepuestos();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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
                    // Ordenar de mayor a menor
                    if (repuestos != null) {
                        Collections.sort(repuestos, (r1, r2) -> Double.compare(r2.getPrecio(), r1.getPrecio()));
                        repuestoAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.nav_price_descending:
                    // Ordenar de menor a mayor
                    if (repuestos != null) {
                        Collections.sort(repuestos, (r1, r2) -> Double.compare(r1.getPrecio(), r2.getPrecio()));
                        repuestoAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.nav_brand:
                    showMarcas();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void fetchRepuestos() {
        Call<List<Repuesto>> call = apiInterface.getRepuestos(null, null);
        call.enqueue(new Callback<List<Repuesto>>() {
            @Override
            public void onResponse(Call<List<Repuesto>> call, Response<List<Repuesto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repuestos = response.body();
                    repuestoAdapter = new RepuestoAdapter(repuestos);
                    recyclerView.setAdapter(repuestoAdapter);
                } else {
                    Log.e("MainActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Repuesto>> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
            }
        });
    }

    private void fetchMarcas() {
        Call<List<Marca>> call = apiInterface.getMarcas();
        call.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    marcas = response.body();
                    marcaAdapter = new MarcaAdapter(marcas, idMarca -> fetchModelos(idMarca));
                    recyclerView.setAdapter(marcaAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } else {
                    Log.e("MainActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
            }
        });
    }

    private void fetchModelos(int idMarca) {
        Call<List<Modelo>> call = apiInterface.getModelosPorMarca(idMarca);
        call.enqueue(new Callback<List<Modelo>>() {
            @Override
            public void onResponse(Call<List<Modelo>> call, Response<List<Modelo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Modelo> modelos = response.body();
                    showModelos(modelos);
                } else {
                    Log.e("MainActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Modelo>> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
            }
        });
    }

    private void showModelos(List<Modelo> modelos) {
        // Crear un nuevo fragmento para mostrar los modelos
        ModeloFragment modeloFragment = ModeloFragment.newInstance(modelos);

        // Reemplazar el fragmento actual con el nuevo fragmento
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, modeloFragment);
        fragmentTransaction.addToBackStack(null); // Opcional: agregar a la pila de retroceso
        fragmentTransaction.commit();

        // Ocultar el RecyclerView de marcas
        recyclerView.setVisibility(View.GONE);
        isShowingModels = true;
    }

    private void showMarcas() {
        // Mostrar el RecyclerView de marcas
        recyclerView.setVisibility(View.VISIBLE);
        // Limpiar el fragmento de modelos si está visible
        if (isShowingModels) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new Fragment()); // Vaciar el frame_layout
            fragmentTransaction.commit();
            isShowingModels = false;
        }
        fetchMarcas();  // Volver a cargar las marcas si es necesario
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void openDrawerLayout(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}



