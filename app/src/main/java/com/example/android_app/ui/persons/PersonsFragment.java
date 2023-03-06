package com.example.android_app.ui.persons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_app.databinding.FragmentPersonsBinding;

public class PersonsFragment extends Fragment {

    private FragmentPersonsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonsViewModel personsViewModel =
                new ViewModelProvider(this).get(PersonsViewModel.class);

        binding = FragmentPersonsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}