package com.example.zoompe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class transactionFragment extends Fragment {

    androidx.appcompat.widget.Toolbar toolbar;
    BottomNavigationView navigationView;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transaction,container,false);
        toolbar = getActivity().findViewById(R.id.mytoolbar);
        toolbar.setTitle("Transations");
        toolbar.setLogo(R.drawable.ic_baseline_history_24_w);
        navigationView = getActivity().findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.history).setChecked(true);
        return root;
    }
}
