package com.example.galleonsup.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.galleonsup.R;
import com.example.galleonsup.databinding.FragmentProfileBinding;
import com.example.galleonsup.model.User;
import com.example.galleonsup.ui.login.LoginActivity;
import com.example.galleonsup.utils.CustomUtility;
import com.example.galleonsup.utils.MySingleton;
import com.ramijemli.percentagechartview.PercentageChartView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileFragment extends Fragment {

    SweetAlertDialog sweetAlertDialog;

    FragmentProfileBinding binding;
    User user;
    SweetAlertDialog pDialog;
    JSONObject jsonObject;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view)
    {

        user = User.getInstance();

        if(user == null)
        {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitle("No user found please login");
            sweetAlertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }

        binding.todayChart.setProgress((float) 70.0,true);
        binding.totalChart.setProgress((float) 30.0,true);

    }


}