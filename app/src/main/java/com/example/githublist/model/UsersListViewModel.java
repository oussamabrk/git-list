package com.example.githublist.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.githublist.R;

import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListViewModel extends ArrayAdapter<GitUser> {
    private int resource;
    public UsersListViewModel(@NonNull Context context, int resource, List<GitUser> data) {
        super(context, resource, data);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if(listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        CircleImageView imageViewUser = listViewItem.findViewById(R.id.imageViewAvatar);
        TextView textViewUsername = listViewItem.findViewById(R.id.textViewUserName);
        TextView textViewScore = listViewItem.findViewById(R.id.textViewScore);

        textViewUsername.setText(getItem(position).username);
        textViewScore.setText(String.valueOf(getItem(position).score));
        try {
            URL url = new URL(getItem(position).avatarUrl);
            Log.i("avatarurl",getItem(position).avatarUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openStream());
            imageViewUser.setImageBitmap(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listViewItem;
    }
}
