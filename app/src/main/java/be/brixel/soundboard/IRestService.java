package be.brixel.soundboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRestService {

    @GET("/api/audiotracks")
    Call<List<Sound>> audioTracks();
    @POST("/api/audiotracks/{id}/play")
    Call<Void> call2(@Path("id") int id);
}
