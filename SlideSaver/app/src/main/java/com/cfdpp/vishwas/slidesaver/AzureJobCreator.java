package com.cfdpp.vishwas.slidesaver;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by vishwas on 26/10/18.
 */

public class AzureJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
//        return null;
        switch (tag) {
//            case AzureService.TAG:
//                return new AzureService();
            default:
                return null;
        }
    }
}
