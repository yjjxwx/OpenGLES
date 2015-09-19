package gles.alex.com.opengles.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import gles.alex.com.opengles.R;

/**
 * Created by alex on 15-9-19.
 */
public class MainActivity extends Activity {
    private GridView mGridView;
    private ActivityAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridView);
        initDatas();
        mAdapter = new ActivityAdapter(this, mDatas);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("yjjxwx", "Position: " + mDatas.get(position));

            }
        });
    }
    private void initDatas() {
        mDatas.add("GaussianBlurActivity");
        mDatas.add("SixPointedStarActivity");
    }



    class ActivityAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mDatas;

        class ItemHold {
            Button button;
        }

        public ActivityAdapter(Context context, List<String> datas) {
            mContext = context;
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view, null);
                ItemHold itemHold = new ItemHold();
                itemHold.button = (Button)convertView.findViewById(R.id.item_button);
                convertView.setTag(itemHold);
                itemHold.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName(MainActivity.this, "gles.alex.com.opengles.activity." +((Button)v).getText());
                        startActivity(intent);
                    }
                });
            }
            ItemHold item = (ItemHold)convertView.getTag();
            item.button.setText(mDatas.get(position));
            return convertView;
        }
    }
}
