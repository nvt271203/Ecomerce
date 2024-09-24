package com.example.fashionecommerce.FragmentAdmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fashionecommerce.Activity.DetailOderActivity;
import com.example.fashionecommerce.ActivityUser.ParametersOderActivity;
import com.example.fashionecommerce.Adapter.StatusOderAdapterAdmin;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogStatusOderBinding;
import com.example.fashionecommerce.databinding.FragmentOderAdminBinding;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.StatusOder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OderAdminFragment extends Fragment implements StatusOderAdapterAdmin.OnClickListener {
    FragmentOderAdminBinding binding;
    private List<Oder> oderList = new ArrayList<>();
    private StatusOderAdapterAdmin statusOderAdapterAdmin;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOderAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configStoreOder();
        configRVoder();

    }

    private void configStoreOder() {
        FirebaseHelper.databaseReference("adminOder")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            oderList.clear();
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                                    Oder oder = dataSnapshot.getValue(Oder.class);
                                    oderList.add(oder);
                            }
                            Collections.reverse(oderList);
                            statusOderAdapterAdmin.notifyDataSetChanged();
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVoder() {
        binding.RVoder.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.RVoder.setHasFixedSize(true);
        statusOderAdapterAdmin = new StatusOderAdapterAdmin((ArrayList<Oder>) oderList,requireContext(), this);
        binding.RVoder.setAdapter(statusOderAdapterAdmin);
    }
    @Override
    public void OnClick(Oder oder, String action) {
        switch (action){
            case "DETAIL" :
                Toast.makeText(requireContext(), action, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), ParametersOderActivity.class);
                intent.putExtra("getPurchasedOder", oder);
                startActivity(intent);
                break;
            case "STATUS" :
                showDialogStatus(oder);
                break;
            default:
                Toast.makeText(requireContext(), "default", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showDialogStatus(Oder oder){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogRetangle);

        DialogStatusOderBinding statusOderBinding = DialogStatusOderBinding.inflate(LayoutInflater.from(getContext()));

        RadioGroup radioGroup = statusOderBinding.radioStatus;
        RadioButton radioWait = statusOderBinding.radioWait;
        RadioButton radioSuccess = statusOderBinding.radioSuccess;
        RadioButton radioRefuse = statusOderBinding.radioRefuse;

        switch (oder.getStatus().toString()){
            case "WAIT" : {
                radioGroup.check(R.id.radioWait);
                radioSuccess.setEnabled(true);
                radioRefuse.setEnabled(true);
                break;
            }
            case "SUCCESS" : {
                radioGroup.check(R.id.radioSuccess);
                radioWait.setEnabled(false);
                radioRefuse.setEnabled(false);
                break;
            }
            default : {
                radioGroup.check(R.id.radioRefuse);
                radioSuccess.setEnabled(false);
                radioWait.setEnabled(false);
                break;
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioWait){
                    oder.setStatus(StatusOder.WAIT);
                }else if (i == R.id.radioSuccess){
                    oder.setStatus(StatusOder.SUCCESS);
                }else if (i == R.id.radioRefuse){
                    oder.setStatus(StatusOder.REFUSE);
                }
            }
        });

        statusOderBinding.btnBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        statusOderBinding.btnConfirm.setOnClickListener(v -> {
            oder.store(false);
            dialog.dismiss();
        });

        builder.setView(statusOderBinding.getRoot());
        dialog = builder.create();

        if (!requireActivity().isFinishing()){
            dialog.show();
        }
    }


}