package com.example.galleonsup.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.galleonsup.A;
import com.example.galleonsup.R;
import com.example.galleonsup.databinding.FragmentProfileBinding;
import com.example.galleonsup.model.User;
import com.example.galleonsup.ui.evaluation.tmrlist.TmrListFragment;
import com.example.galleonsup.ui.login.LoginActivity;
import com.example.galleonsup.utils.StaticTags;

import org.json.JSONObject;



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
        clearAllBackStackFragment();

        user = User.getInstance();

        if(user == null)
        {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitle("No user found please login");
            sweetAlertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    requireActivity().finish();
                }
            });
        }

        Animation animation = new AlphaAnimation(1, (float) 0.80); //to change visibility from visible to invisible
        animation.setDuration(1500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        binding.banner.startAnimation(animation); //to start animation


        binding.todayChart.setProgress((float) 70.0,true);
        binding.totalChart.setProgress((float) 30.0,true);

        binding.totalTmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireActivity().getSupportFragmentManager().findFragmentByTag(StaticTags.TMR_LIST_FRAGMENT_TAG) == null) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.nav_host_fragment, new TmrListFragment())
                            .addToBackStack(StaticTags.TMR_LIST_FRAGMENT_TAG)
                            .commit();
                }
            }
        });

    }

    private void clearAllBackStackFragment() {
        Log.d("stack count", String.valueOf(requireActivity().getSupportFragmentManager().getBackStackEntryCount()));
        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
            requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
