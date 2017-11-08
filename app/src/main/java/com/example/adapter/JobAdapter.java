package com.example.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jobportalw.DemoSignUpActivity;
import com.app.jobportalw.JobDetailsActivity;
import com.app.jobportalw.MyApplication;
import com.app.jobportalw.R;
import com.app.jobportalw.SignUpActivity;
import com.example.item.ItemJob;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.example.util.PopUpAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ItemRowHolder> {

    private ArrayList<ItemJob> dataList;
    private Context mContext;
    MyApplication MyApp;

    public JobAdapter(Context context, ArrayList<ItemJob> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        MyApp = MyApplication.getInstance();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_job, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemJob singleItem = dataList.get(position);
        holder.jobTitle.setText(singleItem.getJobName());
        holder.companyTitle.setText(!MyApp.getIsLogin() ? singleItem.getJobCompanyName() : holder.btn_buy_premium_membership.getContext().getResources().getString(R.string.buy_premium));
    /*    if (!MyApp.getIsLogin()) {
            holder.companyTitle.setTextColor(ContextCompat.getColor(holder.companyTitle.getContext(), R.color.red));
        }*/
        holder.jobDate.setText("Date Posted :- " + singleItem.getJobDate());
        holder.jobDesignation.setText("Designation :- " + singleItem.getJobDesignation());
        holder.jobAddress.setText(MyApp.getIsLogin() ? singleItem.getJobAddress() : holder.btn_buy_premium_membership.getContext().getResources().getString(R.string.visible_to_premium));
        if (!MyApp.getIsLogin()) {
            holder.jobAddress.setTextColor(ContextCompat.getColor(holder.jobAddress.getContext(), R.color.red));
        }


        Picasso.with(mContext).load(singleItem.getJobImage()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpAds.ShowInterstitialAds(mContext);
                Intent intent = new Intent(mContext, JobDetailsActivity.class);
                intent.putExtra("Id", singleItem.getId());
                mContext.startActivity(intent);
            }
        });

        holder.btnApplyJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.getIsLogin()) {
                    new ApplyJob().execute(Constant.APPLY_JOB_URL + MyApp.getUserId() + "&job_id=" + singleItem.getId());
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.need_login), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!MyApp.getIsLogin()) {
            holder.btnApplyJob.setVisibility(View.VISIBLE);
            holder.btn_buy_premium_membership.setVisibility(View.VISIBLE);
            holder.btn_buy_premium_membership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(holder.btn_buy_premium_membership.getContext(), DemoSignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    holder.btn_buy_premium_membership.getContext().startActivity(intent);

                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.btn_buy_premium_membership.getContext().getResources().getString(R.string.premium_url)));
                    holder.btn_buy_premium_membership.getContext().startActivity(browserIntent);*/
                }
            });
        } else {
            holder.btnApplyJob.setVisibility(View.GONE);

            holder.btn_buy_premium_membership.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView jobTitle, companyTitle, jobDate, jobDesignation, jobAddress;
        public LinearLayout lyt_parent;
        public Button btnApplyJob, btn_buy_premium_membership;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            jobTitle = (TextView) itemView.findViewById(R.id.text_job_title);
            companyTitle = (TextView) itemView.findViewById(R.id.text_job_company);
            jobDate = (TextView) itemView.findViewById(R.id.text_job_date);
            jobDesignation = (TextView) itemView.findViewById(R.id.text_job_designation);
            jobAddress = (TextView) itemView.findViewById(R.id.text_job_address);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            btnApplyJob = (Button) itemView.findViewById(R.id.btn_apply_job);
            btn_buy_premium_membership = (Button) itemView.findViewById(R.id.btn_buy_premium_membership);
        }
    }

    private class ApplyJob extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
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
                showToast(mContext.getString(R.string.nodata));
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

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
