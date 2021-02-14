package com.example.galleonsup.ui.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.galleonsup.MainActivity;
import com.example.galleonsup.R;
import com.example.galleonsup.databinding.FragmentNotificationListBinding;
import com.example.galleonsup.model.Notification;
import com.example.galleonsup.model.User;
import com.example.galleonsup.ui.evaluation.tmrlist.TmrListFragment;
import com.example.galleonsup.ui.login.LoginActivity;
import com.example.galleonsup.utils.CustomUtility;
import com.example.galleonsup.utils.MySingleton;
import com.example.galleonsup.utils.StaticTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NotificationFragment extends Fragment {

    SweetAlertDialog sweetAlertDialog;

    FragmentNotificationListBinding binding;
    User user;
    SweetAlertDialog pDialog;
    JSONObject jsonObject;
    RecyclerView recyclerView;
    DataAdapter mAdapter;
    private  ArrayList<Notification> seenDataList = new ArrayList<Notification>();
    private ArrayList<Notification> unseenDataList = new ArrayList<Notification>();
    private  ArrayList<Notification> mainNotificationList = new ArrayList<Notification>();

    boolean isSeenBtnActive = false, isUnseenBtnActive = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view)
    {
        //clearAllBackStackFragment();

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

        recyclerView = binding.notificationListRecycler;
        mAdapter = new DataAdapter(mainNotificationList);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(),1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getNotificationList();


        binding.seenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSeenBtnActive)
                {
                    isSeenBtnActive = true;
                    binding.seenBtn.setBackgroundResource(R.drawable.ic_attendance_active_btn);
                    addSeenNotification();
                }
                else
                {
                    isSeenBtnActive = false;
                    binding.seenBtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                    removeSeenNotification();
                }
            }
        });

        binding.unseenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUnseenBtnActive){
                    isUnseenBtnActive = true;
                    binding.unseenBtn.setBackgroundResource(R.drawable.ic_attendance_active_btn);
                    addUnseenNotification();
                }
                else
                {
                    isUnseenBtnActive = false;
                    binding.unseenBtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                    removeUnseenNotification();
                }
            }
        });


    }

    private void addSeenNotification() {
        if (mainNotificationList.size() != unseenDataList.size()) {
            mainNotificationList.clear();
        }
        mainNotificationList.addAll(seenDataList);
        mAdapter.notifyDataSetChanged();
    }

    private void removeSeenNotification()
    {
        if(mainNotificationList.size() == seenDataList.size())
        {
            mainNotificationList.clear();
        }
        else
        {
            mainNotificationList.clear();
            mainNotificationList.addAll(unseenDataList);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void addUnseenNotification(){
        if (mainNotificationList.size() != seenDataList.size()) {
            mainNotificationList.clear();
        }
        mainNotificationList.addAll(unseenDataList);
        mAdapter.notifyDataSetChanged();
    }
    private void removeUnseenNotification(){
        if(mainNotificationList.size() == unseenDataList.size())
        {
            mainNotificationList.clear();
        }
        else
        {
            mainNotificationList.clear();
            mainNotificationList.addAll(seenDataList);
        }
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



    private void getNotificationList() {


        seenDataList.clear();
        unseenDataList.clear();
/*
        for (int i = 1; i<=10;i++)
        {
            seenDataList.add(new Notification(String.valueOf(i), "Welcome to the new campaign"+i,"This is a new campaign",
                    "www.interspeed.com","url","1"));
        }

        for (int i = 11; i<=20;i++)
        {
            unseenDataList.add(new Notification(String.valueOf(i), "Welcome to the new campaign"+i,"This is a new campaign",
                    "www.interspeed.com","url","0"));
        }



 */
        //
        sweetAlertDialog = new SweetAlertDialog(requireContext(), 5);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        String upLoadServerUri = "https://galleon.atmdbd.com/api/notification/get_user_active_notification_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upLoadServerUri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            sweetAlertDialog.dismiss();
                            Log.e("response", response);
                            jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("success");
                            if (code.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("itemList");
                                for (int i = 0; i<jsonArray.length();i++)
                                {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("has_message_read").equals("0"))
                                    {
                                        unseenDataList.add(new Notification(jsonObject.getString("notification_id"),
                                                jsonObject.getString("notification_name"), jsonObject.getString("message_text"),jsonObject.getString("attachment_url"),
                                                jsonObject.getString("notification_type_name"),jsonObject.getString("has_message_read")));
                                    }
                                    else
                                    {
                                        seenDataList.add(new Notification(jsonObject.getString("notification_id"),
                                                jsonObject.getString("notification_name"), jsonObject.getString("message_text"),jsonObject.getString("attachment_url"),
                                                jsonObject.getString("notification_type_name"),jsonObject.getString("has_message_read")));
                                    }
                                }

                            }
                            else
                                CustomUtility.showError(requireContext(), "No data found", "Failed");
                        } catch (JSONException e) {
                            CustomUtility.showError(requireContext(), e.getMessage(), "Getting Response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sweetAlertDialog.dismiss();
                Log.e("res",error.toString());
                CustomUtility.showError(requireActivity(), "Network Error, try again!", "Failed");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", user.getUserId());
                return params;
            }
        };

        MySingleton.getInstance(requireActivity()).addToRequestQue(stringRequest);

        mainNotificationList.addAll(unseenDataList);
        mainNotificationList.addAll(seenDataList);
        mAdapter.notifyDataSetChanged();

    }


    // data adapter class for showing the list
    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

        private final List<Notification> dataList;
        public DataAdapter(List<Notification> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_row_layout, parent, false);
            return new DataAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
            final Notification data = dataList.get(position);
            holder.serialNumber.setText(String.valueOf(position+1)+".");
            holder.headerText.setText(data.getHeader());
            if(data.getIsSeen().equals("0"))
            {
                holder.rowLayout.setBackgroundResource(R.color.unseen);
            }
            else{
                holder.rowLayout.setBackgroundResource(R.color.seen);
            }
            holder.rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationDetailsShowingFragment showingFragment = new NotificationDetailsShowingFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("notification", dataList.get(position));
                    showingFragment.setArguments(args);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.nav_host_fragment, showingFragment)
                            .addToBackStack(StaticTags.NOTIFICATION_DETAILS_SHOWING_FRAGMENT_TAG)
                            .commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serialNumber, headerText;
            ConstraintLayout rowLayout;
            public MyViewHolder(View convertView) {
                super(convertView);
                serialNumber = convertView.findViewById(R.id.serialNumber);
                headerText = convertView.findViewById(R.id.header);
                rowLayout = convertView.findViewById(R.id.row_layout);
            }
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        //requireContext().stopService(intent);
    }
}
