package com.liuhao.mobilesafe.ui;

import java.util.List;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.domain.ContactInfo;
import com.liuhao.mobilesafe.engine.ContactInfoService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectContactActivity extends Activity {
	
	private ListView lv;
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_contact);
		
		ContactInfoService service = new ContactInfoService(this);
		infos = service.getContactInfos();
		
		lv = (ListView) this.findViewById(R.id.lv_select_contact);
		lv.setAdapter(new SelectContactAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = infos.get(position).getPhone();
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(0, intent);
				finish();
			}
			
		});
		
	}
	
	private class SelectContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactInfo info = infos.get(position);
			LinearLayout ll = new LinearLayout(SelectContactActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView tv_name = new TextView(SelectContactActivity.this);
			tv_name.setText("联系人：" + info.getName());
			tv_name.setTextColor(getResources().getColor(R.color.textcolor));
			TextView tv_phone = new TextView(SelectContactActivity.this);
			tv_phone.setText("电话：" + info.getPhone());
			tv_phone.setTextColor(getResources().getColor(R.color.textcolor));
			ll.addView(tv_name);
			ll.addView(tv_phone);
			return ll;
		}
		
	}
	
}
