package com.example.githublist;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.githublist.model.GitUser;
import com.example.githublist.model.GitUserResponse;
import com.example.githublist.model.UsersListViewModel;
import com.example.githublist.service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public List<GitUser> data = new ArrayList<>();
    public static final String USERNAME_PARAM = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button searchButton = findViewById(R.id.buttonSearch);
        EditText editTextUser = findViewById(R.id.editTextUser);
        ListView listViewUsers = findViewById(R.id.listViewUsers);
        UsersListViewModel listViewModel = new UsersListViewModel(this, R.layout.users_list_view_layout, data);
        listViewUsers.setAdapter(listViewModel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = editTextUser.getText().toString();

                GitRepoServiceAPI service = retrofit.create(GitRepoServiceAPI.class);
                Call<GitUserResponse> gitUserResponseCall = service.searchUsers(q);

                gitUserResponseCall.enqueue(new Callback<GitUserResponse>() {
                    @Override
                    public void onResponse(Call<GitUserResponse> call, Response<GitUserResponse> response) {
                        if(!response.isSuccessful()) {
                            Log.i("error", String.valueOf(response.code()));
                            return;
                        }
                        GitUserResponse gitUserResponse = response.body();
                        for(GitUser user: gitUserResponse.users) {
//                            Log.i("username", user.avatarUrl);
                            data.add(user);
                        }
                        listViewModel.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<GitUserResponse> call, Throwable t) {
                        Log.i("error", "Error onFailure ");
                    }
                });
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = data.get(position).username;

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });

    }
}