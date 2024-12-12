package com.example.top10downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
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
        listApps = (ListView) findViewById(R.id.xmlListView);
        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: Done.. ");
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
            Log.d(TAG, "onPostExecute: Parameter is " +s );

            ParseApplication parseApplication= new ParseApplication();
            parseApplication.parse(s);

/*            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
                    MainActivity.this,
                    R.layout.list_item,
                    parseApplication.getApplications());
            listApps.setAdapter(arrayAdapter);*/

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this,R.layout.list_record,parseApplication.getApplications());
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