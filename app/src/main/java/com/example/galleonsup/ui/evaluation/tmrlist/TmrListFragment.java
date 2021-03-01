package com.example.galleonsup.ui.evaluation.tmrlist;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleonsup.R;

import com.example.galleonsup.databinding.FragmentTmrListBinding;
import com.example.galleonsup.model.Tmr;
import com.example.galleonsup.model.User;
import com.example.galleonsup.utils.SpacesItemDecoration;


import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class TmrListFragment extends Fragment {
    static Bitmap bitmap;
    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;
    SweetAlertDialog sweetAlertDialog;
    RecyclerView recyclerView;
    DataAdapter mAdapter;
    private final ArrayList<Tmr> dataList = new ArrayList<Tmr>();
    private final ArrayList<Tmr> mainRetailList = new ArrayList<Tmr>();
    User user;
    FragmentTmrListBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTmrListBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        user = User.getInstance();
        if(user.getUserId()==null)
        {
            user.setValuesFromSharedPreference(requireActivity().getSharedPreferences("user",MODE_PRIVATE));
        }

        Animation animation = new AlphaAnimation(1, (float) 0.80); //to change visibility from visible to invisible
        animation.setDuration(1500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        binding.banner.startAnimation(animation); //to start animation

        recyclerView = binding.retailListRecycler;
        mAdapter = new DataAdapter(dataList);
        //recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        //recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getTmrList();



        binding.searchRetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getNewSearchedList(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getNewSearchedList(CharSequence s) {
        ArrayList<Tmr> newList = new ArrayList<>();
        if(s.equals(""))
        {
            dataList.clear();
            dataList.addAll(mainRetailList);
        }
        else
        {
            for(int i = 0;i < mainRetailList.size(); i++)
            {
                if(mainRetailList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())
                        || mainRetailList.get(i).getId().toLowerCase().contains(s.toString().toLowerCase())
                        || mainRetailList.get(i).getTeam().toLowerCase().contains(s.toString().toLowerCase()))
                {
                    newList.add(mainRetailList.get(i));
                }
            }
        }

        dataList.clear();
        dataList.addAll(newList);
        mAdapter.notifyDataSetChanged();
    }

    private void getTmrList() {


        dataList.clear();
        /*
        for (int i = 1; i<=50;i++)
        {
            dataList.add(new Retailer("S Alam Telecom "+i, "DHk101322"+i,"43A, 14Rd, Nikunjo 2, Khilkhet, Dhaka",
                    "80.23","89.33","1"));
        }

         */
        dataList.add(new Tmr("Sohanur Rahman", "","2", "43A, 14Rd, Nikunjo 2, Khilkhet, Dhaka",
                "Team 01","40%", "30"));
        dataList.add(new Tmr("Sadat Quayium", "","3", "Hatir Jhil, Dhaka",
                "Team 01","80%", "70"));
        dataList.add(new Tmr("Angshuman Shahu", "","6", "Wary, Dhaka",
                "Team 01","80%", "60"));
        dataList.add(new Tmr("Forkan Uddin", "","7", "Khilgao, Dhaka",
                "Team 01","50%", "40"));
        dataList.add(new Tmr("Sohanur Rahman", "","2", "43A, 14Rd, Nikunjo 2, Khilkhet, Dhaka",
                "Team 01","40%", "30"));
        dataList.add(new Tmr("Sadat Quayium", "","3", "Hatir Jhil, Dhaka",
                "Team 01","80%", "70"));
        dataList.add(new Tmr("Angshuman Shahu", "","6", "Wary, Dhaka",
                "Team 01","80%", "60"));
        dataList.add(new Tmr("Forkan Uddin", "","7", "Khilgao, Dhaka",
                "Team 01","50%", "40"));
        dataList.add(new Tmr("Sohanur Rahman", "","2", "43A, 14Rd, Nikunjo 2, Khilkhet, Dhaka",
                "Team 01","40%", "30"));
        dataList.add(new Tmr("Sadat Quayium", "","3", "Hatir Jhil, Dhaka",
                "Team 01","80%", "70"));
        dataList.add(new Tmr("Angshuman Shahu", "","6", "Wary, Dhaka",
                "Team 01","80%", "60"));
        dataList.add(new Tmr("Forkan Uddin", "","7", "Khilgao, Dhaka",
                "Team 01","50%", "40"));
        mainRetailList.addAll(dataList);
        mAdapter.notifyDataSetChanged();


        /*
        sweetAlertDialog = new SweetAlertDialog(requireContext(), 5);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();
        MySingleton.getInstance(requireContext()).addToRequestQue(new StringRequest(1, "https://fresh.atmdbd.com/api/android/get_brand_list.php", new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    sweetAlertDialog.dismiss();
                    Log.e("response", response);
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("success");
                    if (code.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("brandList");
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            priorBrandList.add(jsonArray.getJSONObject(i).getString("name"));
                            brandMap.put(i,jsonArray.getJSONObject(i).getString("id"));
                        }
                        priorBrandAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, priorBrandList);
                        binding.priorBrandSpinner.setAdapter(priorBrandAdapter);
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

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //requireContext().stopService(intent);
    }
    // data adapter class for showing the list
    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

        private final List<Tmr> dataList;
        public DataAdapter(List<Tmr> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tmr_profile_row_layout, parent, false);
            return new DataAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
            final Tmr data = dataList.get(position);


        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, id, area;
            ConstraintLayout rowLayout;
            public MyViewHolder(View convertView) {
                super(convertView);

                rowLayout = convertView.findViewById(R.id.row_layout);
            }
        }
    }


}
