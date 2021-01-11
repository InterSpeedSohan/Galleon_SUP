package com.example.galleonsup.ui.attendance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.galleonsup.MainActivity;
import com.example.galleonsup.R;
import com.example.galleonsup.databinding.FragmentAttendanceBinding;
import com.example.galleonsup.model.User;
import com.example.galleonsup.utils.CustomUtility;
import com.example.galleonsup.utils.GPSLocation;
import com.example.galleonsup.utils.MySingleton;
import com.example.galleonsup.utils.StaticTags;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AttendanceFragment extends Fragment {

    FragmentAttendanceBinding binding;
    String photoName = "", imageString = "";
    Boolean photoFlag = false;

    static final int REQUEST_IMAGE_CAPTURE = 6;

    SharedPreferences sharedPreferences;

    boolean network = false;
    SweetAlertDialog sweetAlertDialog, pDialog;
    User user;
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String activeBtn = "", fromDate = "", toDate = "",place = "",leaveType = "", leaveTypeId = "",retailerCode = "",reason = "",deviceDate = "";
    public String presentLat = "", presentLon = "", presentAcc = "";

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();

    int animDuration;

    GPSLocation gpsLocation;

    ArrayList<String> leaveTypeList = new ArrayList<>();
    Map<Integer, String> leaveTypeIdMap = new HashMap<>();

    SpinKitView spinKitView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
    }

    public void initialize()
    {
        user = User.getInstance();
        if(user.getUserId()==null)
        {
            user.setValuesFromSharedPreference(requireActivity().getSharedPreferences(StaticTags.USER_PREFERENCE,MODE_PRIVATE));
        }

        gpsLocation = new GPSLocation(requireContext());
        gpsLocation.GPS_Start();
        gpsLocation.setLocationChangedListener(new GPSLocation.LocationChangedListener() {
            @Override
            public void locationChangeCallback(String lat, String lon, String acc) {
                presentLat = lat;
                presentLon = lon;
                presentAcc = acc;
                binding.gpsText.setText(acc);
            }
        });


        Animation animation = new AlphaAnimation(1, (float) 0.80); //to change visibility from visible to invisible
        animation.setDuration(1500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        binding.banner.startAnimation(animation); //to start animation

        animDuration = 1000;

        binding.inbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                activeBtn = "in";
                binding.submitbtn.setVisibility(View.VISIBLE);
                binding.leavebtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                binding.inbtn.setBackgroundResource(R.drawable.ic_attendance_active_btn);
                binding.outbtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                //binding.inOutLayout.setVisibility(View.VISIBLE);
                crossfadeVisible(binding.inOutLayout);
                binding.leaveLayout.setVisibility(View.GONE);
                //binding.lateRemark.setVisibility(View.VISIBLE);
                crossfadeVisible(binding.lateRemark);
                //optionSpinner.setAdapter(adapter);
                photoFlag = false;
                binding.takeSelfieText.getResources().getColor(R.color.text_color);
            }
        });

        binding.outbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeBtn = "out";
                binding.submitbtn.setVisibility(View.VISIBLE);
                binding.leavebtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                binding.inbtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                binding.outbtn.setBackgroundResource(R.drawable.ic_attendance_active_btn);
                //binding.inOutLayout.setVisibility(View.VISIBLE);
                crossfadeVisible(binding.inOutLayout);
                binding.leaveLayout.setVisibility(View.GONE);
                binding.lateRemark.setVisibility(View.GONE);
                //optionSpinner.setAdapter(adapter);
                photoFlag = false;
                binding.takeSelfieText.getResources().getColor(R.color.text_color);
            }
        });


        binding.leavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeBtn = "leave";
                leaveType = "";
                leaveTypeId = "";
                binding.submitbtn.setVisibility(View.VISIBLE);
                binding.leavebtn.setBackgroundResource(R.drawable.ic_attendance_active_btn);
                binding.inbtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                binding.outbtn.setBackgroundResource(R.drawable.ic_attendance_inactive);
                binding.inOutLayout.setVisibility(View.GONE);
                //binding.leaveLayout.setVisibility(View.VISIBLE);
                crossfadeVisible(binding.leaveLayout);
            }
        });


        binding.camera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String photoName = CustomUtility.getDeviceDate() + "image.jpeg";
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });



        final DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }

        };

        binding.fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireContext(), fromdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(myCalendar.compareTo(myCalendar2) == 1)
                {
                    binding.toDate.setText("");
                    CustomUtility.showWarning(requireContext(),"Select correct date","Failed");
                }
                else{
                    updateToDate();
                }

            }

        };

        binding.tobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromDate.equals(""))
                {
                    CustomUtility.showWarning(requireContext(),"Select from date first","Failed");
                }
                else
                {
                    new DatePickerDialog(requireContext(), todate, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });


        binding.leaveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leaveType = binding.leaveSpinner.getSelectedItem().toString();
                leaveTypeId = leaveTypeIdMap.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network = CustomUtility.haveNetworkConnection(requireContext());
                boolean flag = chekFeilds();
                if(flag)
                {
                    SweetAlertDialog confirm = new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE);
                    confirm.setTitle("Are you sure?");
                    confirm.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            confirm.dismissWithAnimation();
                            upload();
                        }
                    });
                    confirm.setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            confirm.dismissWithAnimation();
                        }
                    });
                    confirm.show();
                }

            }
        });

        spinKitView = binding.loadingAnimation;
        getLeaveType();

    }

    private void disableAll()
    {
        binding.inbtn.setEnabled(false);
        binding.outbtn.setEnabled(false);
        binding.leavebtn.setEnabled(false);
    }

    private void enableAll()
    {
        binding.inbtn.setEnabled(true);
        binding.outbtn.setEnabled(true);
        binding.leavebtn.setEnabled(true);
    }

    private void getLeaveType() {
        disableAll();
        spinKitView.setVisibility(View.VISIBLE);
        String upLoadServerUri = "https://rocket.atmdbd.com/api/android/get_attendance_status_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upLoadServerUri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spinKitView.setVisibility(View.GONE);
                        Log.e("response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if(code.equals("true"))
                            {
                                enableAll();
                                jsonArray = jsonObject.getJSONArray("attendanceStatusList");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    leaveTypeList.add(jsonArray.getJSONObject(i).getString("name"));
                                    leaveTypeIdMap.put(i,jsonArray.getJSONObject(i).getString("id"));
                                }
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(requireContext(), R.layout.spinner_item, leaveTypeList);
                                binding.leaveSpinner.setAdapter(adapter2);
                            }
                            else
                            {
                                code = "Failed";
                                CustomUtility.showError(requireContext(),message,code);
                            }


                        } catch (JSONException e) {
                            CustomUtility.showError(requireContext(), e.getMessage(), "Failed");
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                spinKitView.setVisibility(View.GONE);
                Log.e("response",error.toString());
                CustomUtility.showError(requireContext(), "Network slow, try again", "Failed");

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId",user.getUserId());
                params.put("AttendanceGroupId","2");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }

    //after finishing camera intent whether the picture was save or not
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("image","okk");
            photoFlag = true;
            binding.takeSelfieText.setText(R.string.done_message);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageString = CustomUtility.imageToString(imageBitmap);
        }
    }

    private void updateFromDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.e("FromdDate",sdf.format(myCalendar.getTime()));
        fromDate = sdf.format(myCalendar.getTime());
        binding.fromdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateToDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDate = sdf.format(myCalendar2.getTime());
        binding.toDate.setText(sdf.format(myCalendar2.getTime()));
    }

    public String getDeviceDate()
    {
        String myFormat = "yyyy-MM-dd H:m:s"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = new Date();
        return  sdf.format(date);
    }
    private boolean chekFeilds()
    {
        if (!network)
        {
            CustomUtility.showWarning(requireContext(),"Please turn on internet connection","No inerternet connection!");
            return false;
        }
        if(activeBtn.equals(""))
        {
            CustomUtility.showWarning(requireContext(),"Select attendance type In, Out or Leave","Required feild!");
            return false;
        }
        else if(presentAcc.equals(""))
        {
            CustomUtility.showWarning(requireContext(),"Please wait for the gps","Required fields");
            return false;
        }
        else if((activeBtn.equals("in") | activeBtn.equals("out") ) & !photoFlag)
        {
            CustomUtility.showWarning(requireContext(),"Take selfie","Required feild!");
            return false;
        }
        else if(activeBtn.equals("leave") & (fromDate.equals("") | toDate.equals("")))
        {
            CustomUtility.showWarning(requireContext(),"Select form and to date","Required feild!");
            return false;
        }
        else if(activeBtn.equals("leave") & leaveType.equals(""))
        {
            CustomUtility.showWarning(requireContext(), "Select leave type","Required feild!");
            return false;
        }
        else if(activeBtn.equals("leave") & binding.leaveReason.getText().toString().equals(""))
        {
            CustomUtility.showWarning(requireContext(),"Write the reason","Required feild!");
            return false;
        }
        return true;
    }

    private void upload()
    {
        pDialog = new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#08839b"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        String upLoadServerUri;
        if(activeBtn.equals("leave"))
        {
            upLoadServerUri = "https://rocket.atmdbd.com/api/android/insert_leave_submit.php";
        }
        else
        {
            upLoadServerUri = "https://rocket.atmdbd.com/api/android/insert_attendance.php";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upLoadServerUri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.e("response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if(code.equals("true"))
                            {
                                code = "Successful";
                                new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Successful")
                                        .setContentText("")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                startActivity(requireActivity().getIntent());
                                                requireActivity().finish();
                                            }
                                        })
                                        .show();
                            }
                            else
                            {
                                code = "Failed";
                                CustomUtility.showError(requireContext(),message,code);
                                //CustomUtility.showError(AttendanceActivity.this,"You allready submitted in",code);
                            }


                        } catch (JSONException e) {
                            CustomUtility.showError(requireContext(), e.getMessage(), "Failed");
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response",error.toString());
                CustomUtility.showError(requireContext(), "Network slow, try again", "Failed");

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId",user.getUserId());
                params.put("AttendanceType",activeBtn);
                if(activeBtn.equals("leave"))
                {
                    params.put("LeaveType",leaveType);
                    params.put("LeaveTypeId",leaveTypeId);
                    params.put("LeaveDateStart",fromDate);
                    params.put("LeaveDateEnd",toDate);
                    params.put("Reason",binding.leaveReason.getText().toString());
                }
                else
                {
                    params.put("DeviceDate",getDeviceDate());
                    params.put("Latitude", presentLat);
                    params.put("Longitude",presentLon);
                    params.put("Accuracy",presentAcc);
                    params.put("PictureData",imageString);
                    params.put("Remark",binding.lateRemark.getText().toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }

    private void crossfadeVisible(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.animate()
                .alpha(1f)
                .setDuration(animDuration)
                .setListener(null);
    }

}
