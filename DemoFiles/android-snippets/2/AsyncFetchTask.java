package com.cloudtasks;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.cloudtasks.shared.TaskProxy;

public class AsyncFetchTask extends AsyncTask<Long, Void, List<TaskProxy>> {

    private final CloudTasksActivity activity;
    private boolean newTask = false;

    public AsyncFetchTask(CloudTasksActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected List<TaskProxy> doInBackground(Long... arguments) {
        final List<TaskProxy> list = new ArrayList<TaskProxy>();

        // INSERT RPC HERE!
        


        return list;
    }

    @Override
    protected void onPostExecute(List<TaskProxy> result) {
    	if (newTask) {
    		activity.addTasks(result);
    	} else {
    		activity.setTasks(result);
    	}
    }
}
