package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFragment extends Fragment {

    private ArrayList<ItemData> list;
    private MyRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        getData();

        return v;
    }


    private void getData(){
        List<String> title = Arrays.asList("갈비찜",
                "갈비탕", "갈치조림","감자탕", "고등어조림","김밥", "김치볶음밥"
                ,"김치찌개", "김치찜", "꽁치조림","닭볶음탕", "도토리묵","동태찌개", "된장찌개", "떡볶이","만두국","매운탕","무국","미역국","북엇국","비빔밥"
                ,"삼계탕","새우볶음밥","설렁탕","소세지볶음","수제비","순두부찌개","시래기국","알밥","열무국수","육개장","잔치국수","잡채","장조림"
                ,"제육볶음","주꾸미볶음","찜닭","코다리조림","콩국수","파전","해물찜");
        List<Integer> img = Arrays.asList(
                R.drawable.f1,
                R.drawable.f2,
                R.drawable.f3,
                R.drawable.f4,
                R.drawable.f5,
                R.drawable.f6,
                R.drawable.f7,
                R.drawable.f8,
                R.drawable.f77, //김치찜
                R.drawable.f9,
                R.drawable.f10,
                R.drawable.f11,
                R.drawable.f12,
                R.drawable.f13,
                R.drawable.f14,
                R.drawable.f15,
                R.drawable.f16,
                R.drawable.f17,
                R.drawable.f18,
                R.drawable.f19,
                R.drawable.f20,
                R.drawable.f21,
                R.drawable.f22,
                R.drawable.f23,
                R.drawable.f24,
                R.drawable.f25,
                R.drawable.f26,
                R.drawable.f27,
                R.drawable.f28,
                R.drawable.f29,
                R.drawable.f30,
                R.drawable.f31,
                R.drawable.f32,
                R.drawable.f33,
                R.drawable.f34,
                R.drawable.f35,
                R.drawable.f36,
                R.drawable.f37,
                R.drawable.f38,
                R.drawable.f39,
                R.drawable.f40
        );
        for(int i=0; i<title.size(); i++){
            ItemData data = new ItemData();
            data.setName(title.get(i));
            data.setImage(img.get(i));
            adapter.addItem(data);

        }
        adapter.notifyDataSetChanged();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}