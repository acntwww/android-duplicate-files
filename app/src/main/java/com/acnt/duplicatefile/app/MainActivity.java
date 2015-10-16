package com.acnt.duplicatefile.app;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.acnt.duplicatefile.app.base.BaseActivity;
import com.acnt.duplicatefile.app.config.API;
import com.tencent.bugly.crashreport.CrashReport;
import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import trikita.log.Log;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private ListView mLvFolders;

    private List<String> mFolderPath = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

    private static final int REQUEST_DIRECTORY = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        mLvFolders = (ListView) findViewById(R.id.folders);
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mFolderPath
                );
        mLvFolders.setAdapter(mAdapter);

        mLvFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mFolderPath.get(position);
                Intent intent = new Intent(mAct, AnalizeActivity.class);
                intent.putExtra(API.DIR_NAME, path);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_folder) {
            selectFolder();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectFolder() {
        final Intent chooserIntent = new Intent(
                this,
                DirectoryChooserActivity.class);

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("DirChooserSample")
                .allowReadOnlyDirectory(false)
                .allowNewDirectoryNameModification(true)
                .initialDirectory(Environment.getExternalStorageDirectory().getPath())
                .build();

        chooserIntent.putExtra(
                DirectoryChooserActivity.EXTRA_CONFIG,
                config);

        startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String dir = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                Log.d("onActivityResult", dir);
                mFolderPath.add(dir);
                mAdapter.notifyDataSetChanged();

            }
        }
    }
}
