package com.example.galleonsup.ui.notification;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.galleonsup.databinding.FragmentNotificationDetailsShowingBinding;
import com.example.galleonsup.model.Notification;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NotificationDetailsShowingFragment extends Fragment {
    FragmentNotificationDetailsShowingBinding binding;
    SweetAlertDialog sweetAlertDialog;
    JSONObject jsonObject;
    Notification notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationDetailsShowingBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        Animation animation = new AlphaAnimation(1, (float) 0.80); //to change visibility from visible to invisible
        animation.setDuration(1500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        binding.banner.startAnimation(animation); //to start animation


        assert getArguments() != null;
        notification = getArguments().getParcelable("notification");

        binding.textNotificationHeader.setText(notification.getHeader());
        binding.textNotificationBody.setText(notification.getBody());
        binding.textUrl.setText(notification.getUrl());
        binding.textUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }


}
