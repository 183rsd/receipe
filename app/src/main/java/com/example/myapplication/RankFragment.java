package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapters.PostAdapter;
import com.example.myapplication.models.Post;

import java.util.ArrayList;
import java.util.List;

public class RankFragment extends Fragment {

    private RecyclerView mPostRecyclerView;

    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_rank, container, false);

        mPostRecyclerView = v.findViewById(R.id.post_recyclerview);
        mDatas = new ArrayList<>();
        mDatas.add(new Post(null, "title","contents"));
        mDatas.add(new Post(null, "title","contents"));
        mDatas.add(new Post(null, "title","contents"));

        mAdapter = new PostAdapter(mDatas);
        mPostRecyclerView.setAdapter(mAdapter);

        v.findViewById(R.id.post_edit).setOnClickListener((View.OnClickListener) getActivity());


        return v;
    }
    public void onClick(View view){

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}