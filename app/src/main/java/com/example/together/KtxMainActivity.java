package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.together.fragment.KtxMyPostsFragment;
import com.example.together.fragment.KtxPostsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class KtxMainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[] {
					new KtxPostsFragment(),
					new KtxMyPostsFragment(),
			};

			@Override
			public Fragment getItem(int position) {
				return mFragments[position];
			}
			@Override
			public int getCount() {
				return mFragments.length;
			}
			@Override
			public CharSequence getPageTitle(int position) {
				return getResources().getStringArray(R.array.headings1)[position];
			}
		};

		ViewPager mViewPager = findViewById(R.id.container);
		mViewPager.setAdapter(mPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

		findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(KtxMainActivity.this, KtxNewPostActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_logout:
				FirebaseAuth.getInstance().signOut();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}