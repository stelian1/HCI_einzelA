package com.example.stelian.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by stelian on 06.04.2015.
 */
public class ResultActivity extends ActionBarActivity implements View.OnClickListener {

    private Button returnButton;
    private TextView resultView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.result);

        returnButton = (Button) this.findViewById(R.id.returnButton);

        // add listener to the return button
        returnButton.setOnClickListener(this);

        resultView = (TextView) this.findViewById(R.id.resultView);

        // create an Intent object in order to retrieve the saved session attributes
        Intent intent = this.getIntent();

        // retrieve the calculated amount in the MainActivity class that was stored int the session attribute
        double result = intent.getDoubleExtra("convertedAmount", 2.0);
        if (intent.getStringExtra("currencyChoice").toString().equals("...")){
            // display error message
            this.resultView.setText("CURRENCY NOT CHOSEN!");
        } else {
            // display converted amount
            this.resultView.setText(intent.getDoubleExtra("originalAmount", 1.0) + " EUR = " + result + " " + intent.getStringExtra("currencyChoice"));
        }
    }

    /**
     * when the returne button is clicked, this method is called
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        // prepare transition to the MainActivity class
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);

        // do the transition
        this.startActivity(intent);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
