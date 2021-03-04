package com.example.galleonsup.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.galleonsup.model.Tmr;
import com.example.galleonsup.utils.StaticTags;

import java.net.URL;
import java.util.ArrayList;

public class Repository {
    private MutableLiveData<ArrayList<Tmr>> allTmrList = new MutableLiveData<ArrayList<Tmr>>();
    private MutableLiveData<ArrayList<Tmr>> presentTmrList = new MutableLiveData<ArrayList<Tmr>>();
    private MutableLiveData<ArrayList<Tmr>> absentTmrList = new MutableLiveData<ArrayList<Tmr>>();
    private MutableLiveData<ArrayList<Tmr>> onLeaveTmrList = new MutableLiveData<ArrayList<Tmr>>();
    private MutableLiveData<ArrayList<Tmr>> idleTmrList = new MutableLiveData<ArrayList<Tmr>>();
    private MutableLiveData<String> tagOfTmrFragment  = new MutableLiveData<>();

    private Thread mThread;

    public LiveData<String> getTagOfTmrFragment() {
        return tagOfTmrFragment;
    }

    public LiveData<ArrayList<Tmr>> getAllTmrList(){
        return allTmrList;
    }

    public LiveData<ArrayList<Tmr>> getPresentTmrList() {return presentTmrList;}
    public LiveData<ArrayList<Tmr>> getAbsentTmrList() {return absentTmrList;}
    public LiveData<ArrayList<Tmr>> getOnLeaveTmrList() {return onLeaveTmrList;}
    public LiveData<ArrayList<Tmr>> getIdleTmrList() {return idleTmrList;}

    public void setTagOfTmrFragment(String tag) {
        tagOfTmrFragment.setValue(tag);
    }

    public void searchTmrList(final URL url) {
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                queryTmrListApi(url);
            }
        };

        if(mThread != null)
        {
            mThread.interrupt();
        }
        mThread = new Thread(fetchJsonRunnable);
        mThread.start();
    }

    private void queryTmrListApi(URL url)
    {
        try {
            ArrayList<Tmr> tmrs = new ArrayList<>();
            ArrayList<Tmr> ptmrs = new ArrayList<>();
            ArrayList<Tmr> atmrs = new ArrayList<>();
            ArrayList<Tmr> ltmrs = new ArrayList<>();
            ArrayList<Tmr> itmrs = new ArrayList<>();
            for (int i=0;i<10;i++)
            {
                tmrs.add(new Tmr("Sadat Quayium Apu","",String.valueOf(i),"Gulshan-2, Dhaka",
                        "team"+i,"30", "50","4", "10",StaticTags.PRESENT_SATATUS));
                ptmrs.add(tmrs.get(tmrs.size()-1));
            }
            presentTmrList.postValue(ptmrs);
            for (int i=0;i<3;i++)
            {
                tmrs.add(new Tmr("Md. Sohanur Rahman","",String.valueOf(i),"nikunjo-2, Khilkhet",
                        "team"+i,"30", "50","4", "10",StaticTags.ABSENT_STATUS));
                atmrs.add(tmrs.get(tmrs.size()-1));
            }
            absentTmrList.postValue(atmrs);
            for (int i=0;i<4;i++)
            {
                tmrs.add(new Tmr("Md. Forkan Uddin","",String.valueOf(i),"Gulshan-2, Dhaka",
                        "team"+i,"30", "50","4", "10",StaticTags.ON_LEAVE_STATUS));
                ltmrs.add(tmrs.get(tmrs.size()-1));
            }
            onLeaveTmrList.postValue(ltmrs);
            for (int i=0;i<2;i++)
            {
                tmrs.add(new Tmr("Aungshuman Shahu","",String.valueOf(i),"Wary",
                        "team"+i,"30", "50","4", "10", StaticTags.IDLE_STATUS));
                itmrs.add(tmrs.get(tmrs.size()-1));
            }
            idleTmrList.postValue(itmrs);
            allTmrList.postValue(tmrs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
