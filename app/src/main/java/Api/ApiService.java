package Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private Retrofit retrofit;
    private ApiInterface apiInterface;

    public ApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.22:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }
}

