package com.example.book_search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class BookSearch extends AsyncTask<String,Void,String>
{
    private Context context;
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";
    private URL url = null;
    private Uri uri = null;
    private HttpURLConnection connection;
    public BookSearch(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream stream ;
        try {
             uri = Uri.parse(strings[0]).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, strings[1])
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

                url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setRequestMethod("GET");
                connection.connect();

                stream = connection.getInputStream();
                BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
                StringBuilder s = new StringBuilder();
                String line="";

                while((line = buf.readLine())!=null) s.append(line+"\n");

                if(s.length() == 0) return null;

                buf.close();
                if(stream!=null)
                {
                    try {stream.close();}
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return s.toString();
        }
        catch(Exception e)
        {
            Log.i("Exception",e.getMessage());
        }
        finally {
            if(connection!=null)
                connection.disconnect();
        }
        return null;
    }

    //There is a chance that the doInBackground() method won't return the expected JSON string. For example, the try/catch might fail and throw an exception,
    // the network might time out, or other unhandled errors might occur. In those cases, the JSON parsing will fail and will throw an exception.
    // To handle this case, do the JSON parsing in a try/catch block, and handle the case where incorrect or incomplete data is returned.//

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        try
        {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i = 0;
            String title = null;
            String authors = null;
            while (i < itemsArray.length() && (authors == null && title == null))
            {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title"); // Extracting all information contains in title section(See Json text for more info)
                    authors = volumeInfo.getString("authors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i++;
            }
            if (title != null && authors != null) {
               MainActivity.textView1.setText("Book : " +title);
               MainActivity.textView2.setText("Author : " +authors);
            }
            else
            {
                MainActivity.textView1.setText(R.string.no_results);
                MainActivity.textView2.setText(" ");
            }
        }
        catch(Exception e)
        {
            MainActivity.textView1.setText(R.string.no_results);
            MainActivity.textView2.setText(" ");
            Log.i("Execute","ERROR");
        }
    }
}
