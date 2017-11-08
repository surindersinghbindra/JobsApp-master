package com.app.jobportalw;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.button_link_indian)
    Button buttonLinkIndian;
    @BindView(R.id.button_mobile_pay)
    Button buttonMobilePay;
    @BindView(R.id.button_paypal)
    Button buttonPaypal;
    @BindView(R.id.button_paymart)
    Button buttonPaymart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_link_indian)
    public void onButtonLinkIndianClicked() {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url)));
        startActivity(browserIntent);

    }

    @OnClick(R.id.button_mobile_pay)
    public void onButtonMobilePayClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url_mobilepay)));
        startActivity(browserIntent);
    }

    @OnClick(R.id.button_paypal)
    public void onButtonPaypalClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url_paypal)));
        startActivity(browserIntent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick(R.id.button_paymart)
    public void onViewClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_url_paypal)));
        startActivity(browserIntent);
    }
}
