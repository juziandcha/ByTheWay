package com.example.wmj.bytheway;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

public class ByTheWayActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton mBtnFloat;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    //used for tablayout
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFPAdapter mMyFPAdapter;
    private TabLayout.Tab tab_order;
    private TabLayout.Tab tab_history;
    private TabLayout.Tab tab_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bytheway);

        initInstances();
        setTabLayout();

        //设置透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        showPasswordDialog();

    }

    private void showPasswordDialog() {
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.setOnDialogClick(new LoginDialogFragment.DialogClickListener() {
            @Override
            public void onDialogClick(String Name, String Password) {
                if(Name.equals("cancel")&&Password.equals("cancel"))
                    finish();
                else{
                    Toast.makeText(ByTheWayActivity.this,Name+Password,Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginDialogFragment.show(getFragmentManager(), "LoginDialogFragment");
    }



    private void initInstances() {
        mToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNavigationView=findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.navItem1:
                    case R.id.navItem2:
                    case R.id.navItem3:
                    case R.id.navItem4:
                        Toast.makeText(ByTheWayActivity.this, "Test", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(ByTheWayActivity.this, mDrawerLayout, R.string.hello_world, R.string.hello_world);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnFloat =findViewById(R.id.btn_float);
        mBtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "HelloWorld", Snackbar.LENGTH_SHORT)
                        .setAction("Test",new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ByTheWayActivity.this, "Yes, It works!", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });


    }
    private void setTabLayout(){
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mMyFPAdapter = new MyFPAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMyFPAdapter);

        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        tab_order = mTabLayout.getTabAt(0);
        tab_history = mTabLayout.getTabAt(1);
        tab_chat = mTabLayout.getTabAt(2);

        //给tab设置图标
        tab_order.setIcon(R.drawable.ic_bookmark_border_white_48dp);
        tab_history.setIcon(R.drawable.ic_history_white_48dp);
        tab_chat.setIcon(R.drawable.ic_chat_white_48dp);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bytheway, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

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
}
