package com.example.top10downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    private String feedCachedUrl = "INVALIDATED";
    public static final String STATE_URL="feedUrl";
    public static final String STATE_LIMIT="feedLimit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listApps = findViewById(R.id.xmlListView);
/*        Log.d(TAG, "onCreate: starting Async-task");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: Done.. ");*/
        if (savedInstanceState!=null){
            feedUrl = savedInstanceState.getString(STATE_URL);
            feedLimit = savedInstanceState.getInt(STATE_LIMIT);
        }
        downloadUrl(String.format(feedUrl,feedLimit));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: Inflating menu");
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        if (feedLimit==10){
            menu.findItem(R.id.mnu10).setChecked(true);
        }else {
            menu.findItem(R.id.mnu25).setChecked(true);
        }
        return true; // Return true to display the menu
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.mnuFree) {
            //Log.d(TAG, "onOptionsItemSelected: Free selected");
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
        } else if (id == R.id.mnuPaid) {
            //Log.d(TAG, "onOptionsItemSelected: Paid selected");
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
        } else if (id == R.id.mnuSongs) {
            //Log.d(TAG, "onOptionsItemSelected: Songs selected");
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
        } else if (id==R.id.mnu10) {
            if (!item.isChecked()){
                item.setChecked(true);
                feedLimit = 10;
                Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+"setting feedLimit to "+feedLimit);
            }else {
                Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+"feedLimit unchanged");
            }
        } else if (id==R.id.mnu25) {
             if (!item.isChecked()){
                 item.setChecked(true);
                 feedLimit=35-feedLimit;
                 Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+"setting feedLimit to "+ feedLimit);
             }else {
                 Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+"feedLimit unchanged");
             }
        } else if (id==R.id.mnuRefresh) {
            feedCachedUrl = "INVALIDATED";
        } else {
            //Log.d(TAG, "onOptionsItemSelected: Default case hit");
            return super.onOptionsItemSelected(item);
        }
        /*switch (id){
            case R.id.mnuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
                break;
            case R.id.mnuPaid:
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml";
                break;
            case R.id.mnuSongs:
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
                break;

            default:
                return super.onOptionsItemSelected(item);


        }*/
        downloadUrl(String.format(feedUrl,feedLimit));
        Log.d(TAG, "onOptionsItemSelected: Menu Created ");
//        getMenuInflater().inflate(R.menu.feed_menu, item.getSubMenu());
        return true;
    }

    @Override//saving the state after the life cycle is destroyed.
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_URL,feedUrl);
        outState.putInt(STATE_LIMIT,feedLimit);
        super.onSaveInstanceState(outState);
    }

    private void downloadUrl(String feedUrl){
        if (!feedUrl.equalsIgnoreCase(feedCachedUrl)){
            Log.d(TAG, "onCreate: starting Async-task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedUrl);
            feedCachedUrl = feedUrl;
            Log.d(TAG, "downloadUrl: Done.. ");
        }else {
            Log.d(TAG, "downloadUrl: URL not changed ");
        }
    } 

    public class DownloadData extends AsyncTask<String,Void, String> {
        private static final String TAG = "DownloadData";
        private Context context;

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with "+ strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading" );
            }
            return rssFeed;

        }

/*        public DownloadData(Context context) {
            this.context = context;
        }*/

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, "onPostExecute: Parameter is " +s );

            ParseApplication parseApplication= new ParseApplication();
            parseApplication.parse(s);

/*            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
                    MainActivity.this,
                    R.layout.list_item,
                    parseApplication.getApplications());
            listApps.setAdapter(arrayAdapter);*/

            FeedAdapter <FeedEntry>feedAdapter = new FeedAdapter<>(MainActivity.this,R.layout.list_record,parseApplication.getApplications());
            listApps.setAdapter(feedAdapter);
        }

        private String downloadXML(String urlPath){
            StringBuilder xmlResult = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was: "+ response);
/*            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);*/
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int charsRead;
                char [] inputBuffer = new char[500];
                while (true){
                    charsRead = reader.read(inputBuffer);
                    if (charsRead<0){
                        break;
                    }
                    if (charsRead>0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0 ,charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            }catch (MalformedURLException e ){
                Log.e(TAG, "downloadXML: Invalid URL"+ e.getMessage() );
            }
            catch (IOException e){
                Log.e(TAG, "downloadXML: IO exception reading data: "+e.getMessage() );
            }
            catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security exception: needs permission" + e.getMessage() );
                //e.printStackTrace();
            }
            return null;
        }
    }

}