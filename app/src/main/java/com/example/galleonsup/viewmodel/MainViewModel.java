package com.example.galleonsup.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.galleonsup.model.Tmr;
import com.example.galleonsup.repository.Repository;

import java.net.URL;


public class MainViewModel extends ViewModel {
    private Repository repository = new Repository();
    private LiveData<Tmr[]> repositoryTmrList = repository.getTmrList();
    private LiveData<String> tagOfTmrFragment = repository.getTagOfTmrFragment();


    public LiveData<String> getTagOfTmrFragment() {
        return tagOfTmrFragment;
    }

    public void setTagOfTmrFragment(String tag)
    {
        repository.setTagOfTmrFragment(tag);
    }

    public LiveData<Tmr[]> getRepositoryTmrList() {
        return repositoryTmrList;
    }

    public void searchRepositoryTmrList(URL url) {
        repository.searchTmrList(url);
    }
}
