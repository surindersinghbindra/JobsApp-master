package com.app.jobportalw;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 35, trim = true, message = "Enter Valid Full Name")
    EditText edtFullName;

    @Required(order = 3)
    @Email(order = 4, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    @Required(order = 5)
    @Password(order = 6, message = "Enter a Valid Password")
    @TextRule(order = 7, minLength = 6, message = "Enter a Password Correctly")
    EditText edtPassword;

    @TextRule(order = 8, message = "Enter valid Phone Number", minLength = 0, maxLength = 14)
    EditText edtMobile;

    @TextRule(order = 9, message = "Enter City", minLength = 0, maxLength = 35)
    EditText edtCity;

    @TextRule(order = 10, message = "Enter Address", minLength = 0, maxLength = 100)
    EditText edtAddress;

    private Validator validator;

    MyApplication MyApp;

    String strFullname, strEmail, strPassword, strMobi, strCity, strAddress;

    Toolbar toolbar;
    ScrollView mScrollView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_edit_profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MyApp = MyApplication.getInstance();

        edtFullName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtMobile = (EditText) findViewById(R.id.edt_phone);
        edtCity = (EditText) findViewById(R.id.edt_city);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

        if (JsonUtils.isNetworkAvailable(EditProfileActivity.this)) {
            new MyTaskProfile().execute(Constant.USER_PROFILE_URL + MyApp.getUserId());
        } else {
            showToast(getString(R.string.conne_msg1));
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        strFullname = edtFullName.getText().toString().replace(" ", "%20");
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();
        strMobi = edtMobile.getText().toString();
        strCity = edtCity.getText().toString().replace(" ", "%20");
        strAddress = edtAddress.getText().toString().replace(" ", "%20");

        if (JsonUtils.isNetworkAvailable(EditProfileActivity.this)) {
            new MyTaskUpdate().execute(Constant.USER_PROFILE_UPDATE_URL + strFullname + "&email=" + strEmail + "&password=" + strPassword + "&phone=" + strMobi + "&user_id=" + MyApp.getUserId() + "&city=" + strCity + "&address=" + strAddress);
        } else {
            showToast(getString(R.string.conne_msg1));
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyTaskProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        edtFullName.setText(objJson.getString(Constant.USER_NAME));
                        edtEmail.setText(objJson.getString(Constant.USER_EMAIL));
                        edtMobile.setText(objJson.getString(Constant.USER_PHONE));
                        if (!objJson.getString(Constant.USER_CITY).isEmpty()) {
                            edtCity.setText(objJson.getString(Constant.USER_CITY));
                        }
                        if (!objJson.getString(Constant.USER_ADDRESS).isEmpty()) {
                            edtAddress.setText(objJson.getString(Constant.USER_ADDRESS));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private class MyTaskUpdate extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        }
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast("Failed");
        } else {
            showToast("Your Profile Updated");
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        menu.getItem(0).setIcon(R.drawable.ic_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_edit:
                validator.validateAsync();
                break;
            case R.id.menu_logout:
                Logout();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void Logout() {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyApp.saveIsLogin(false);
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                        //  .setIcon(R.drawable.ic_logout)
                .show();
    }
}
