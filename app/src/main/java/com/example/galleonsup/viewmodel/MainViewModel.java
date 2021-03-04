package com.example.galleonsup.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.galleonsup.model.Tmr;
import com.example.galleonsup.repository.Repository;

import java.net.URL;
import java.util.ArrayList;


public class MainViewModel extends ViewModel {
    private Repository repository = new Repository();
    private LiveData<ArrayList<Tmr>> repositoryTmrList = repository.getAllTmrList();
    private LiveData<String> tagOfTmrFragment = repository.getTagOfTmrFragment();
    private LiveData<ArrayList<Tmr>> repositoryPresentTmrList = repository.getPresentTmrList();
    private LiveData<ArrayList<Tmr>> repositoryAbsentTmrList = repository.getAbsentTmrList();
    private LiveData<ArrayList<Tmr>> repositoryOnLeaveTmrList = repository.getOnLeaveTmrList();
    private LiveData<ArrayList<Tmr>> repositoryIdleTmrList = repository.getIdleTmrList();


    public LiveData<String> getTagOfTmrFragment() {
        return tagOfTmrFragment;
    }

    public void setTagOfTmrFragment(String tag)
    {
        repository.setTagOfTmrFragment(tag);
    }

    public LiveData<ArrayList<Tmr>> getRepositoryTmrList() {
        return repositoryTmrList;
    }

    public LiveData<ArrayList<Tmr>> getRepositoryOnLeaveTmrList() {
        return repositoryOnLeaveTmrList;
    }

    public LiveData<ArrayList<Tmr>> getRepositoryPresentTmrList() {
        return repositoryPresentTmrList;
    }

    public LiveData<ArrayList<Tmr>> getRepositoryAbsentTmrList() {
        return repositoryAbsentTmrList;
    }

    public LiveData<ArrayList<Tmr>> getRepositoryIdleTmrList() {
        return repositoryIdleTmrList;
    }


    public void searchRepositoryTmrList(URL url) {
        repository.searchTmrList(url);
    }
}
