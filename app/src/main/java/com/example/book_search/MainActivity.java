package com.example.book_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    static EditText editText;
    static TextView textView1,textView2;
    private static final String url = "https://www.googleapis.com/books/v1/volumes?";
    BookSearch bookSearch;
    ConnectivityManager manager;
    NetworkInfo info;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.edit);
        textView1 = (TextView)findViewById(R.id.getTitle);
        textView2 = (TextView)findViewById(R.id.getAuthor);
        firebaseAuth = FirebaseAuth.getInstance();
    }

// The user experience of searching is not intuitive. When the user taps the button, the keyboard remains visible,
// and the user has no way of knowing that the query is in progress.
// One solution is to programmatically hide the keyboard and update one of the result text views to read "Loading..." while the query is performed.

    public void SearchBook(View view)
    {
        InputMethodManager inputManager = (InputMethodManager) // Used to hide the keyboard
                getSystemService(Context.INPUT_METHOD_SERVICE);

        String query = editText.getText().toString();

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        if(manager!=null)
        {
            info = manager.getActiveNetworkInfo();
        }
        if(info!=null && info.isConnected() && (int)query.length() != 0)
        {
            bookSearch = new BookSearch(this);
            bookSearch.execute(url,query);
            textView1.setText("Loading");
            textView2.setText(" ");
        }
        else if((int)query.length() == 0)
        {
            textView1.setText("No Search Term");
            textView2.setText(" ");
        }
        else
        {
            Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show();
        }
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(this,Login.class));
        finish();
    }
}