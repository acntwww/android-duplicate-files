package com.acnt.duplicatefile.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.acnt.duplicatefile.app.base.BaseActivity;
import com.acnt.duplicatefile.app.config.API;
import com.acnt.duplicatefile.app.util.MD5;
import com.acnt.duplicatefile.app.util.ToastUtil;
import trikita.log.Log;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class AnalizeActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "AnalizeActivity";

    private String mPath;

    private ListView mLvDuplicateFiles;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDuplicateFiles = new ArrayList<String>();
    private Map<String, ArrayList<String>> mAllFiles = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_analize);
        mPath = getIntent().getStringExtra(API.DIR_NAME);
        setTitle(mPath);

        mLvDuplicateFiles = (ListView) findViewById(R.id.duplicate_files);
        mAdapter = new ArrayAdapter<String>(mAct,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mDuplicateFiles);
        mLvDuplicateFiles.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.analize) {
            analize();
            return true;
        }

        if (id == R.id.delete) {
            deleteFiles();
        }

        return super.onOptionsItemSelected(item);
    }


    private void analize() {
        mAllFiles.clear();
        mDuplicateFiles.clear();
        mAdapter.notifyDataSetChanged();

        new AnalizeTask().execute();
    }

    private void deleteFiles() {
        if (mDuplicateFiles.isEmpty()) {
            ToastUtil.showShortToast(mAct, "Please Exe Analize Task before..");
            return;
        }

        new DeleteFileTask().execute();
    }

    private class DeleteFileTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mAct, null, "Deletinnnng....", true, false);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Iterator<String> iterator = mDuplicateFiles.iterator();
                while (iterator.hasNext()) {
                    String f = iterator.next();
                    new File(f).delete();
                    iterator.remove();
                    publishProgress(0);
                    //TimeUnit.SECONDS.sleep(3);
                }

            } catch (Exception ex) {
                Log.e("doInBackground", ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
            if (mDuplicateFiles.isEmpty()) {
                ToastUtil.showShortToast(mAct, "Success.");
            }

        }
    }

    private class AnalizeTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mAct, null, "Analizinnng....", true, false);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                File file = new File(mPath);
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            continue;
                        }
                        String md5 = MD5.calculateMD5(f);
                        ArrayList<String> fs;
                        if (!mAllFiles.containsKey(md5)) {
                            fs = new ArrayList<String>();
                            mAllFiles.put(md5, fs);
                        }
                        fs = mAllFiles.get(md5);
                        fs.add(f.getPath());
                        Collections.sort(fs, new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return lhs.length() - rhs.length();
                            }
                        });
                    }
                } else {
                    //ToastUtil.showShortToast(mAct, "Direction is Empty");
                    return null;
                }


                for (String k : mAllFiles.keySet()) {
                    ArrayList<String> fs = mAllFiles.get(k);
                    if (fs.size() > 1) {
                        mDuplicateFiles.addAll(fs.subList(1, fs.size()));
                    }
                }

                Log.d("doInBackground", "mAllFiles", mAllFiles);
            } catch (Exception e) {
                Log.e("doInBackground", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
            if (mDuplicateFiles.isEmpty()) {
                ToastUtil.showShortToast(mAct, "No Duplicate Files.");
            }
        }
    }


}
