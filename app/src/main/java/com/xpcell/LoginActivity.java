package com.xpcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    private EditText userTxt;
    private EditText password;
    private Button loginbton;
    private ApiService apiService;
    private ProgressDialog progressDialog;
    private TextView ctaNueva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userTxt = findViewById(R.id.userTxt);
        password = findViewById(R.id.password);
        loginbton = findViewById(R.id.loginbton);
        ctaNueva = findViewById(R.id.ctaNuevaBton);
        apiService = new ApiService();

        loginbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = userTxt.getText().toString();
                String contrasena = password.getText().toString();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Usuario usuarioEnviar = new Usuario(usuario, contrasena);
                    realizarLogin(usuarioEnviar);
                }
            }
        });
        ctaNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void realizarLogin(Usuario usuario) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();

        ApiService apiService = new ApiService();
        ApiInterface apiInterface = apiService.getApiInterface();

        Call<Respuesta> call = apiInterface.validarUsuario(usuario);
        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                progressDialog.dismiss();
                Log.d("LoginActivity", "Response code: " + response.code());

                if (response.isSuccessful()) {
                    Respuesta respuesta = response.body();
                    Log.d("LoginActivity", "Respuesta del servidor: " + respuesta);
                    if (respuesta != null) {
                        Log.d("LoginActivity", "Success: " + respuesta.isSuccess());
                        if (respuesta.isSuccess()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("LoginActivity", "Response body is null");
                    }
                } else {
                    Log.e("LoginActivity", "Error: " + response.message());
                    try {
                        Log.e("LoginActivity", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("LoginActivity", "Error reading error body", e);
                    }
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("LoginActivity", "Failure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
