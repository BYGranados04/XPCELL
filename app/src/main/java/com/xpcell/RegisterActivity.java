package com.xpcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import Api.ApiInterface;
import Api.ApiService;
import Api.Respuesta;
import Model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText userTxt;
    private EditText password;
    private Button registerButton;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userTxt = findViewById(R.id.userTxt);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerBtton);

        ApiService apiService = new ApiService();
        apiInterface = apiService.getApiInterface();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = userTxt.getText().toString();
                String contrasena = password.getText().toString();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Usuario usuarioEnviar = new Usuario(usuario, contrasena);
                    registrarUsuario(usuarioEnviar);
                }
            }
        });
    }

    private void registrarUsuario(Usuario usuario) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        Call<Respuesta> call = apiInterface.registrarUsuario(usuario);
        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                progressDialog.dismiss();
                Log.d("RegisterActivity", "Response code: " + response.code());

                if (response.isSuccessful()) {
                    Respuesta respuesta = response.body();
                    Log.d("RegisterActivity", "Respuesta del servidor: " + respuesta);
                    if (respuesta != null) {
                        // Utiliza el método correcto de la clase Respuesta
                        String mensaje = respuesta.getMensaje(); // Asegúrate de que este método exista
                        Toast.makeText(RegisterActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        if (response.code() == 201) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("RegisterActivity", "Response body is null");
                    }
                } else {
                    Log.e("RegisterActivity", "Error: " + response.message());
                    try {
                        Log.e("RegisterActivity", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "Error reading error body", e);
                    }
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("RegisterActivity", "Failure: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
