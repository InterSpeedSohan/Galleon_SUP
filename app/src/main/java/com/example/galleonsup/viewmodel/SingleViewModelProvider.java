package com.example.galleonsup.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class SingleViewModelProvider {
    private MainViewModel mainViewModel;
    Context context;

    public SingleViewModelProvider(Context c)
    {
        context = c;
    }

    public MainViewModel getMainViewModel(Context context) {
        if(mainViewModel == null)
        {
            mainViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainViewModel.class);
        }
        return mainViewModel;
    }
}
