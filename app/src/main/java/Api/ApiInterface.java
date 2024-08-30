package Api;

import java.util.List;

import Model.Marca;
import Model.Modelo;
import Model.Repuesto;
import Model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("/validar_usuario")
    Call<Respuesta> validarUsuario(@Body Usuario usuario);

    @POST("/registrar_usuario")
    Call<Respuesta> registrarUsuario(@Body Usuario usuario);

    @GET("/repuestos")
    Call<List<Repuesto>> getRepuestos(@Query("id_modelo") Integer idModelo, @Query("id_tipo_repuesto") Integer idTipoRepuesto);

    @GET("repuestos")
    Call<List<Repuesto>> getRepuestos(
            @Query("sort") String sort // Parámetro para ordenar los repuestos
    );

    @GET("marcas")
    Call<List<Marca>> getMarcas();

    @GET("modelos/marca/{id_marca}")
    Call<List<Modelo>> getModelosPorMarca(@Path("id_marca") int idMarca);

}