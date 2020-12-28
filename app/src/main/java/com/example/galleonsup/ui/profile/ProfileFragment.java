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

    RecyclerView recyclerView;
    DataAdapter mAdapter;
    SweetAlertDialog sweetAlertDialog;

    FragmentProfileBinding binding;
    User user;
    SweetAlertDialog pDialog;
    JSONObject jsonObject;


    ContextCompat contextCompat;



    private List<UploadDetails> dataList = new ArrayList<>();

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
        binding.textName.setText(user.getName());
        binding.txtTeam.setText("Team: "+user.getTeamName());
        binding.txtMarket.setText("Market: "+user.getArea());


        //getStatus();
        //getList("Dec");
        //createChart();


    }

    // data adapter class for showing the list
    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

        private List<UploadDetails> dataList;

        public DataAdapter(List<UploadDetails> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upload_details_row, parent, false);
            return new DataAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final UploadDetails data = dataList.get(position);

            holder.date.setText(data.getDate());
            holder.time.setText(data.getTime());

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView date,time;
            ConstraintLayout rowLayout;
            public MyViewHolder(View convertView) {
                super(convertView);
                rowLayout = convertView.findViewById(R.id.rowLayout);
                date = convertView.findViewById(R.id.date);
                time = convertView.findViewById(R.id.time);
            }
        }
    }



    public void getStatus() {
        sweetAlertDialog = new SweetAlertDialog(requireContext(), 5);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();
        MySingleton.getInstance(requireContext()).addToRequestQue(new StringRequest(1, "https://fresh.atmdbd.com/api/contact/user_status.php", new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    sweetAlertDialog.dismiss();
                    Log.e("response", response);
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("success");
                    if (code.equals("true")) {
                        float today = 0, total = 0, dailyTarget = 0, totalTarget = 0;
                        today = Float.parseFloat(jsonObject.getString("todayCount"));
                        total = Float.parseFloat(jsonObject.getString("totalCount"));
                        binding.todayAchievment.setText("Today Count: "+jsonObject.getString("todayCount"));
                        binding.totalAchievment.setText("Total Count: "+jsonObject.getString("totalCount"));
                        dailyTarget = Float.parseFloat(jsonObject.getString("dailyTarget"));
                        totalTarget = Float.parseFloat(jsonObject.getString("totalTarget"));
                        binding.todayTarget.setText("Today Target: "+ jsonObject.getString("dailyTarget"));
                        binding.totalTarget.setText("Total Target: "+jsonObject.getString("totalTarget"));
                        PercentageChartView todayChart = binding.todayChart;
                        if(today<=dailyTarget)
                            todayChart.setProgress((float) ((today/dailyTarget) *100.0),true);
                        else
                            todayChart.setProgress((float) (100.0),true);
                        todayChart.apply();

                        PercentageChartView totalChart = binding.totalChart;
                        if(total<=totalTarget)
                            totalChart.setProgress((float) ((total/totalTarget) *100.0),true);
                        else
                            totalChart.setProgress((float) (100.0),true);
                        totalChart.apply();
                    }
                    else
                        CustomUtility.showError(requireContext(), "No data found", "Failed");
                } catch (JSONException e) {
                    CustomUtility.showError(requireContext(), e.getMessage(), "Getting Response");
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                sweetAlertDialog.dismiss();
                final SweetAlertDialog s = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);
                s.setConfirmText("Ok");
                s.setTitleText("Network Error, try again!");
                s.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        s.dismissWithAnimation();
                        startActivity(requireActivity().getIntent());
                        requireActivity().finish();
                    }
                });
                s.show();
            }
        }) {
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", user.getUserId());
                return params;
            }
        });
    }


}