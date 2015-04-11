package com.example.stelian.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 *
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // variables

    private static JSONObject jsonO = null;

    private static String fixerURL = null;

    private static ProgressDialog progressDialog;

    String currencyChoice;

    double price = 0;

    Toast toast = null;

    private CurrencyCreator currencyCreator = null;

    private List<String> types = null;

    private Button changeButton;

    private Spinner dropDown;

    private EditText currencyText;


    // setter and getter
    public static String getFixerURL() {
        return fixerURL;
    }

    public static void setFixerURL(String fixerURL) {
        MainActivity.fixerURL = fixerURL;
    }

    public static JSONObject getJsonO() {
        return jsonO;
    }

    public static void setJsonO(JSONObject jsonO) {
        MainActivity.jsonO = jsonO;
    }

    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public static void setProgressDialog(ProgressDialog progressDialog) {
        MainActivity.progressDialog = progressDialog;
    }

    public Button getChangeButton() {
        return changeButton;
    }

    public void setChangeButton(Button changeButton) {
        this.changeButton = changeButton;
    }

    public Spinner getDropDown() {
        return dropDown;
    }

    public void setDropDown(Spinner dropDown) {
        this.dropDown = dropDown;
    }

    public EditText getCurrencyText() {
        return currencyText;
    }

    public void setCurrencyText(EditText currencyText) {
        this.currencyText = currencyText;
    }


    // methods:

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binds the class with the xml layout
        setContentView(R.layout.activity_main);

        this.setFixerURL("http://api.fixer.io/latest");

        // start the retrieval of the currencies from the API
        new DataReader().execute();

        // binds the button object with the layout button through the id
        changeButton = (Button) this.findViewById(R.id.changeButton);

        // add a listener to the button
        changeButton.setOnClickListener(this);

        dropDown = (Spinner) this.findViewById(R.id.currencySpinner);

        currencyText = (EditText) this.findViewById(R.id.currencyText);

        currencyCreator = new CurrencyCreator();

        // retrieves the currencies from the CurrencyCreator class
        types = currencyCreator.getTypes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);

        // sets the layout ressource
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // binds an adapter to the spinner
        this.dropDown.setAdapter(adapter);
//        this.dropDown.setSelection(1);

        // add a listener to the spinner
        this.dropDown.setOnItemSelectedListener(this);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
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

    /**
     * when the button is pressed, the next method is called
     * @param v
     */
    @Override
    public void onClick(View v) {

        // prepare the transition to the result page
        Intent intent = new Intent(this.getApplicationContext(), ResultActivity.class);

        // save the chosen currency as session attribute so that retrival is possible in the result class
        intent.putExtra("currencyChoice", currencyChoice);

        Double amount = null;

        try {
            // save the amount that was input by the user
            amount = Double.parseDouble(currencyText.getText().toString());

            // convert the input amount into the chosen currency
            double convertedAmount = amount * price;

            // save input amount and calculated amount as session attributes
            intent.putExtra("originalAmount", amount);
            intent.putExtra("convertedAmount", convertedAmount);

            // make the transition to the ResultActivity class
            this.startActivity(intent);
        } catch (NumberFormatException e) {
//            Toast.makeText(MainActivity.this, "NO AMOUNT SELECTED!", Toast.LENGTH_SHORT).show();

            // if no amount was input by the user, display a warning message, but not if ths is already being displayed
            if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                toast = Toast.makeText(getApplicationContext(),
                        "NO AMOUNT SELECTED!", Toast.LENGTH_SHORT);
                toast.show();
            }
            e.printStackTrace();
        }
    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // save the item chosen by user
        currencyChoice = parent.getItemAtPosition(position).toString();
        if (!(jsonO == null)) {
            try {
                // get the rate from the fixer API
                price = jsonO.getDouble(currencyChoice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private class DataReader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            AbstractHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(fixerURL);
            HttpResponse httpResponse = null;
            try {
                // get data from fixer API
                httpResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            String entityText = null;
            try {
                // convert the result into string
                entityText = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // get the rate of the chosen currency
                jsonO = new JSONObject(entityText).getJSONObject("rates");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         *
         */
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         *
         * @param result
         */
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
