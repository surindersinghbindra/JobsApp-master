package com.app.jobportalw;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.DatabaseHelper;
import com.example.item.ItemJob;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class JobDetailsActivity extends AppCompatActivity {

    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ItemJob objBean;
    TextView jobTitle, companyTitle, jobDate, jobDesignation, jobAddress, jobVacancy, jobPhone, jobMail, jobWebsite, jobSkill, jobQualification, jobSalary;
    WebView jobDesc;
    ImageView image;
    String Id;
    DatabaseHelper databaseHelper;
    Button btnSave;
    AdView mAdView;
    Button btnApplyJob, btn_buy_premium_membership;
    MyApplication MyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        objBean = new ItemJob();
        Intent i = getIntent();
        Id = i.getStringExtra("Id");
        databaseHelper = new DatabaseHelper(getApplicationContext());
        MyApp = MyApplication.getInstance();
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        image = (ImageView) findViewById(R.id.image);
        jobTitle = (TextView) findViewById(R.id.text_job_title);
        companyTitle = (TextView) findViewById(R.id.text_job_company);
        jobDate = (TextView) findViewById(R.id.text_job_date);
        jobDesignation = (TextView) findViewById(R.id.text_job_designation);
        jobAddress = (TextView) findViewById(R.id.text_job_address);
        jobPhone = (TextView) findViewById(R.id.text_phone);
        jobWebsite = (TextView) findViewById(R.id.text_website);
        jobMail = (TextView) findViewById(R.id.text_email);
        jobDesc = (WebView) findViewById(R.id.text_job_description);
        jobQualification = (TextView) findViewById(R.id.text_job_qualification);
        jobSkill = (TextView) findViewById(R.id.text_job_skill);
        jobVacancy = (TextView) findViewById(R.id.text_vacancy);
        jobSalary = (TextView) findViewById(R.id.text_job_salary);

        btnSave = (Button) findViewById(R.id.btn_save_job);
        btnApplyJob = (Button) findViewById(R.id.btn_apply_job);

        btn_buy_premium_membership = (Button) findViewById(R.id.btn_buy_premium_membership);
        if (!MyApp.getIsLogin()) {
            btnApplyJob.setVisibility(View.VISIBLE);
            btn_buy_premium_membership.setVisibility(View.VISIBLE);
            btn_buy_premium_membership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JobDetailsActivity.this, DemoSignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                   /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url)));
                    btn_buy_premium_membership.getContext().startActivity(browserIntent);*/
                }
            });
        } else {
            btnApplyJob.setVisibility(View.GONE);

            btn_buy_premium_membership.setVisibility(View.GONE);
        }

        if (JsonUtils.isNetworkAvailable(JobDetailsActivity.this)) {
            new JobDetails().execute(Constant.SINGLE_JOB_URL + Id);
        } else {
            showToast(getString(R.string.conne_msg1));
        }

        jobMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmail();
            }
        });

        jobWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite();
            }
        });

        jobPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues fav = new ContentValues();
                if (databaseHelper.getFavouriteById(Id)) {
                    databaseHelper.removeFavouriteById(Id);
                    btnSave.setText(getString(R.string.save_job));
                    Toast.makeText(JobDetailsActivity.this, getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                } else {
                    fav.put(DatabaseHelper.KEY_ID, Id);
                    fav.put(DatabaseHelper.KEY_TITLE, objBean.getJobName());
                    fav.put(DatabaseHelper.KEY_IMAGE, objBean.getJobImage());
                    fav.put(DatabaseHelper.KEY_COMPANY_NAME, objBean.getJobCompanyName());
                    fav.put(DatabaseHelper.KEY_DATE, objBean.getJobDate());
                    fav.put(DatabaseHelper.KEY_DESIGNATION, objBean.getJobDesignation());
                    fav.put(DatabaseHelper.KEY_LOCATION, objBean.getJobAddress());
                    databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                    btnSave.setText(getString(R.string.save_job_already));
                    Toast.makeText(JobDetailsActivity.this, getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnApplyJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.getIsLogin()) {
                    if (JsonUtils.isNetworkAvailable(JobDetailsActivity.this)) {
                        new ApplyJob().execute(Constant.APPLY_JOB_URL + MyApp.getUserId() + "&job_id=" + Id);
                    } else {
                        showToast(getString(R.string.conne_msg1));
                    }
                } else {
                    Toast.makeText(JobDetailsActivity.this, getString(R.string.need_login), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class JobDetails extends AsyncTask<String, Void, String> {

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
                        objBean.setId(objJson.getString(Constant.JOB_ID));
                        objBean.setJobName(objJson.getString(Constant.JOB_NAME));
                        objBean.setJobCompanyName(objJson.getString(Constant.JOB_COMPANY_NAME));
                        objBean.setJobDate(objJson.getString(Constant.JOB_DATE));
                        objBean.setJobDesignation(objJson.getString(Constant.JOB_DESIGNATION));
                        objBean.setJobAddress(objJson.getString(Constant.JOB_ADDRESS));
                        objBean.setJobImage(objJson.getString(Constant.JOB_IMAGE));
                        objBean.setJobVacancy(objJson.getString(Constant.JOB_VACANCY));
                        objBean.setJobPhoneNumber(objJson.getString(Constant.JOB_PHONE_NO));
                        objBean.setJobMail(objJson.getString(Constant.JOB_MAIL));
                        objBean.setJobCompanyWebsite(objJson.getString(Constant.JOB_SITE));
                        objBean.setJobDesc(objJson.getString(Constant.JOB_DESC));
                        objBean.setJobSkill(objJson.getString(Constant.JOB_SKILL));
                        objBean.setJobQualification(objJson.getString(Constant.JOB_QUALIFICATION));
                        objBean.setJobSalary(objJson.getString(Constant.JOB_SALARY));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {
        firstFavourite();
        jobTitle.setText((objBean.getJobName()));
        companyTitle.setText(MyApp.getIsLogin() ? objBean.getJobCompanyName() : getString(R.string.buy_premium));
        if (!MyApp.getIsLogin()) {
            companyTitle.setTextColor(ContextCompat.getColor(companyTitle.getContext(), R.color.red));
        }
        jobDate.setText("Date Posted :- " + objBean.getJobDate());
        jobDesignation.setText("Designation :- " + objBean.getJobDesignation());
        jobAddress.setText("Address :- " + (MyApp.getIsLogin() ? objBean.getJobAddress() : getString(R.string.visible_to_premium)));
        jobPhone.setText("Phone :- " + (MyApp.getIsLogin() ? objBean.getJobPhoneNumber() : getString(R.string.visible_to_premium)));
        jobWebsite.setText("Website :- " + (MyApp.getIsLogin() ? objBean.getJobCompanyWebsite() : getString(R.string.visible_to_premium)));
        jobMail.setText("Email :- " + (MyApp.getIsLogin() ? objBean.getJobMail() : getString(R.string.visible_to_premium)));
        jobQualification.setText(objBean.getJobQualification());
        jobSkill.setText(objBean.getJobSkill());
        jobVacancy.setText("Vacancy :- " + objBean.getJobVacancy());
        Picasso.with(JobDetailsActivity.this).load(objBean.getJobImage()).into(image);
        jobSalary.setText(objBean.getJobSalary());

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = objBean.getJobDesc();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #9E9E9E;text-align:left;font-size:14px;margin-left:0px}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        jobDesc.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void showToast(String msg) {
        Toast.makeText(JobDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_edit:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        objBean.getJobName() + "\n" +
                                "Company Name :- " + objBean.getJobCompanyName() + "\n" +
                                "Designation :- " + objBean.getJobDesignation() + "\n" +
                                "Phone :- " + objBean.getJobPhoneNumber() + "\n" +
                                "Email :- " + objBean.getJobMail() + "\n" +
                                "Website :- " + objBean.getJobCompanyWebsite() + "\n" +
                                "Address :- " + objBean.getJobAddress() + "\n\n" +
                                "Download Application here https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void openWebsite() {
        if (MyApp.getIsLogin())
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(addHttp(objBean.getJobCompanyWebsite()))));
    }

    private void openEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", objBean.getJobMail(), null));
        emailIntent
                .putExtra(Intent.EXTRA_SUBJECT, "Apply for the post " + objBean.getJobDesignation());
        if (MyApp.getIsLogin())
            startActivity(Intent.createChooser(emailIntent, "Send suggestion..."));
    }

    protected String addHttp(String string1) {
        // TODO Auto-generated method stub
        if (string1.startsWith("http://"))
            return String.valueOf(string1);
        else
            return "http://" + String.valueOf(string1);
    }

    private void dialNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", objBean.getJobPhoneNumber(), null));
        if (MyApp.getIsLogin())
            startActivity(intent);
    }

    private void firstFavourite() {
        if (databaseHelper.getFavouriteById(Id)) {
            btnSave.setText(getString(R.string.save_job_already));
        } else {
            btnSave.setText(getString(R.string.save_job));
        }
    }

    private class ApplyJob extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(JobDetailsActivity.this);
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
                        showToast(objJson.getString(Constant.MSG));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
