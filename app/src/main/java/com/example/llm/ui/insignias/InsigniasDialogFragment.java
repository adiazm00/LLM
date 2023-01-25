package com.example.llm.ui.insignias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.llm.databinding.FragmentInsigniasBinding;

public class InsigniasDialogFragment extends DialogFragment {

    private FragmentInsigniasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInsigniasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)*8/9;
        getDialog().getWindow().setLayout(width, height);
    }
}