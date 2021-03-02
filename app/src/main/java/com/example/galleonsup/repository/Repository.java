package com.example.galleonsup.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.galleonsup.model.Tmr;

import java.net.URL;

public class Repository {
    private MutableLiveData<Tmr[]> allTmrList = new MutableLiveData<>();
    private MutableLiveData<String> tagOfTmrFragment  = new MutableLiveData<>();

    private Thread mThread;

    public LiveData<String> getTagOfTmrFragment() {
        return tagOfTmrFragment;
    }

    public LiveData<Tmr[]> getTmrList(){
        return allTmrList;
    }

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
            Tmr[] tmrs = new Tmr[5];
            for (int i = 0;i<5;i++)
            {
                tmrs[i] = new Tmr("sohan","",String.valueOf(i),"khilkhet",
                        "team"+i,"30","40");
            }
            allTmrList.postValue(tmrs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
