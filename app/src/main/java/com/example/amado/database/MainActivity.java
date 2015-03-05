package com.example.amado.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private DataBaseManager mManager;
    private Cursor mCursor;
    private ListView mContactList;
    private SimpleCursorAdapter mAdapter;
    private EditText mSearchText;
    private Button mSearchButton;
    private String mSearchName;
    private Cursor mSearchCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchText = (EditText)findViewById(R.id.search_text);
        mSearchButton= (Button)findViewById(R.id.search_button);

        mManager = new DataBaseManager(this);
        if(mManager==null){
            createPerson("jaime", "666");
            createPerson("jose", "444");
            createPerson("tool", "888");
            createPerson("fredy", "999");
        }



        mCursor = mManager.loadContactsCursor();
        mContactList = (ListView)findViewById(R.id.contact_list);
        String[] from = new String[]{mManager.CN_NAME, mManager.CN_PHONE};
        int[] to =  new int[]{android.R.id.text1, android.R.id.text2};

        mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                mCursor, from, to,0 );
        mContactList.setAdapter(mAdapter);


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchName = mSearchText.getText().toString();
                new searchTask().execute();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createPerson(String name, String phone){
        mManager.insert(name, phone);
    }


    public class searchTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Searching..", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            mSearchCursor = mManager.searchContact(mSearchName);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!mSearchName.isEmpty()) {
                mAdapter.changeCursor(mSearchCursor);
            }else{
                mCursor=mManager.loadContactsCursor();
                mAdapter.changeCursor(mCursor);
            }
            Toast.makeText(getApplicationContext(), "Search is done", Toast.LENGTH_LONG).show();

        }
    }

}
