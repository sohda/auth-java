//
//  Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
//  See LICENSE for more information.
//

package com.ricohapi.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ricohapi.auth.AuthClient;
import com.ricohapi.auth.CompletionHandler;
import com.ricohapi.auth.Scope;
import com.ricohapi.auth.entity.AuthResult;


public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private TextView resultText;
    private AuthClient authClient;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.statusText = (TextView) findViewById(R.id.statusText);
        this.resultText = (TextView) findViewById(R.id.resultText);

    }

    public void onConnectBtnClick(View view){
        // Create and set an AuthClient object.
        authClient = new AuthClient("### enter your client ID ###", "### enter your client secret ###");
        authClient.setResourceOwnerCreds("### enter your user id ###", "### enter your user password ###");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Start a session.
                authClient.session(Scope.MSTORAGE, new CompletionHandler<AuthResult>(){

                    // Success
                    @Override
                    public void onCompleted(AuthResult result) {
                        Log.i("Connection established", "Here is your access token: " + result.getAccessToken());
                        final String accessToken = result.getAccessToken();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView("Connected!", "Here is your access token: \n" + accessToken);
                            }
                        });
                    }

                    // Error
                    @Override
                    public void onThrowable(final Throwable t) {
                        Log.i("Connection failed.", t.toString());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView("Failed!", "Here is the error: \n" + t.toString());
                            }
                        });
                    }
                });
                return null;
            }
        }.execute();
    }

    private void updateTextView(String status, String result) {
        if (statusText != null && resultText != null) {
            statusText.setText(status);
            resultText.setText(result);
        }
    }
}
