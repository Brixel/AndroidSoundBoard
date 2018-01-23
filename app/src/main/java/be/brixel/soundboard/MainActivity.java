package be.brixel.soundboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String WEBSERVER_URL = "http://10.94.213.241:1880/";

    private IRestService restService;

    private ListView listView;

    private Callback<Void> playSoundCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(MainActivity.this, "failed to connect to sterver", Toast.LENGTH_SHORT).show();
            Log.e(TAG, t.getMessage());
        }
    };

    private Callback<List<Sound>> listCallback = new Callback<List<Sound>>() {
        @Override
        public void onResponse(Call<List<Sound>> call, Response<List<Sound>> response) {
            List<Sound> music = response.body();

            ArrayList<HashMap<String, String>> data = new ArrayList<>();
            for (Sound obj : music) {
                HashMap<String, String> map = new HashMap<>();
                map.put("trackName", obj.trackName);
                data.add(map);
            }

            String[] from = {"trackName"};
            int[] to = {R.id.track_name};

            SimpleAdapter adapter =
                    new SimpleAdapter(
                            MainActivity.this,
                            data,
                            R.layout.list_item,
                            from,
                            to);

            listView.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<List<Sound>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "failed to connect to sterver", Toast.LENGTH_SHORT).show();
            Log.e(TAG, t.getMessage());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEBSERVER_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        restService = retrofit.create(IRestService.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                restService.call2(i).enqueue(playSoundCallback);
            }
        });

        restService.audioTracks().enqueue(listCallback);
    }

}
