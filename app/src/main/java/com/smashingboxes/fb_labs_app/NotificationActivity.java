package com.smashingboxes.fb_labs_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smashingboxes.fb_labs_app.helpers.TextHelper;
import com.smashingboxes.fb_labs_app.network.models.NotificationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class NotificationActivity extends Activity {

    ListView list;
    NotificationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        list = (ListView) findViewById(R.id.list);
        mAdapter = new NotificationsAdapter(this);
        mAdapter.addAll(getDummyModels());
        list.setAdapter(mAdapter);
    }

    private List<NotificationModel> getDummyModels() {
        List<NotificationModel> mList = new ArrayList<>();
        int[] types = {R.string.cancel, R.string.delay, R.string.reschedule};
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            mList.add(new NotificationModel("austin@smashingboxes.com", random.nextLong(), getString(types[i % types.length])));
        }
        return mList;
    }

    public static class NotificationsAdapter extends ArrayAdapter<NotificationModel> {

        static class ViewHolder {
            View sideBar;
            TextView email;
            TextView time;
            TextView status;
        }

        Map<String, Integer> sideBarColorMap = new HashMap<>();

        public NotificationsAdapter(Context context) {
            super(context, -1, new ArrayList<NotificationModel>());
            initColorMap(context);
        }

        private void initColorMap(Context context) {
            Resources res = context.getResources();
            sideBarColorMap.put(res.getString(R.string.cancel), res.getColor(R.color.cancel_button));
            sideBarColorMap.put(res.getString(R.string.reschedule), res.getColor(R.color.reschedule_button));
            sideBarColorMap.put(res.getString(R.string.delay), res.getColor(R.color.delay_button));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            NotificationModel current = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.status = (TextView) convertView.findViewById(R.id.list_item_status);
                holder.email = (TextView) convertView.findViewById(R.id.list_item_email);
                holder.time = (TextView) convertView.findViewById(R.id.list_item_time);
                holder.sideBar = convertView.findViewById(R.id.list_right_banner);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String status = current.getStatus();
            Integer statusColor = sideBarColorMap.get(status.toLowerCase(Locale.US));
            holder.sideBar.setBackgroundColor(statusColor);
            holder.status.setText(current.getStatus());
            holder.status.setTextColor(statusColor);
            holder.email.setText(current.getEmail());
            holder.time.setText(TextHelper.formatTime(current.getTs()));

            return convertView;
        }
    }


}
