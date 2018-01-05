package com.example.wmj.bytheway.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wmj.bytheway.ConnSup.MinaThread;
import com.example.wmj.bytheway.Dialogs.Dialog_changedata;
import com.example.wmj.bytheway.Dialogs.Dialog_createtask;
import com.example.wmj.bytheway.InfoClass.UserData;
import com.example.wmj.bytheway.Dialogs.LoginDialogFragment;
import com.example.wmj.bytheway.Util.MyFPAdapter;
import com.example.wmj.bytheway.R;

import org.apache.mina.core.session.IoSession;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private TabLayout.Tab tab_friends;

    //全局session，用于网络连接
    public static IoSession session=null;
    public static String dataResult=null;

    //condition,用于加锁
    public static Lock lock=new ReentrantLock();
    public static Condition condition=lock.newCondition();

    //用于存储用户信息
    public static UserData userData=new UserData();

    //用于在其他地方调用主Activity
    private static ByTheWayActivity byTheWayActivity;
    public ByTheWayActivity(){
        byTheWayActivity=this;
    }
    public static ByTheWayActivity getByTheWayActivity() {
        return byTheWayActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bytheway);

        initInstances();//初始化各部分界面
        setTabLayout();//初始化四个栏目

        //设置透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        showPasswordDialog();

        new Thread(new MinaThread()).start();

    }
    //登录界面设置界面
    public void showPasswordDialog() {
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
    //新建任务列表设置界面
    private void showcreateTaskDialog() {
        Dialog_createtask dialog_createtask=new Dialog_createtask();
        dialog_createtask.setOnDialogClick(new Dialog_createtask.DialogClickListener() {
            @Override
            public void onDialogClick(boolean ifcreate, String titlename, String contents, String s_address, String e_address) {
                if(ifcreate){
                    Toast.makeText(ByTheWayActivity.this,titlename+contents+s_address+e_address,Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog_createtask.show(getFragmentManager(),"Dialog_createtask");
    }

    //初始化各部分界面
    private void initInstances() {
        //设置使用ToolBar代替ActionBar
        mToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //设置导航栏中各个按钮的监听事件
        mNavigationView=findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //TODO: 注意，该部分添加时要隐藏导航栏
                int id=item.getItemId();
                switch (id){
                    case R.id.menu_about:
                            mDrawerLayout.closeDrawers();//先隐藏导航栏，再调用另外一个界面
                            Intent intent=new Intent(ByTheWayActivity.this, AboutActivity.class);
                            startActivity(intent);
                            break;
                    case R.id.menu_profile:
                            Dialog_changedata dialog_changedata=new Dialog_changedata();
                            dialog_changedata.show(getFragmentManager(),"Dialog_changedata");
                            break;
                    case R.id.menu_help:
                    case R.id.menu_logout:
                            //ToDO: 清空四个栏目的信息，并通知adapter发生了修改
                            confirmDialog();
                            break;
                      default:
                          break;
                }
                return false;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(ByTheWayActivity.this, mDrawerLayout, R.string.hello_world, R.string.hello_world);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //显示左上角按钮
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置悬浮按钮的点击事件
        mBtnFloat =findViewById(R.id.btn_float);
        mBtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showcreateTaskDialog();
                /*
                Snackbar.make(view, "HelloWorld", Snackbar.LENGTH_SHORT)
                        .setAction("Test",new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(ByTheWayActivity.this, "Yes, It works!", Toast.LENGTH_SHORT).show();

                            }
                        }).show();
                   */
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
        tab_friends=mTabLayout.getTabAt(3);

        //给tab设置图标
        tab_order.setIcon(R.drawable.ic_bookmark_border_white_48dp);
        tab_history.setIcon(R.drawable.ic_history_white_48dp);
        tab_chat.setIcon(R.drawable.ic_chat_white_48dp);
        tab_friends.setIcon(R.drawable.ic_account_circle_white_48dp);
    }

    //确认注销Dialog
    private void confirmDialog(){
        //初始化AlertDialog构建器对象
        AlertDialog.Builder builder=new AlertDialog.Builder(ByTheWayActivity.this);
        //设置dialog信息
        builder.setMessage("确定注销吗?");
        //设置dialog标题
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //如果用户单击确认退出,则对话框消失,程序关闭
                dialog.dismiss();
                mDrawerLayout.closeDrawers();//隐藏导航栏
                userData=new UserData();//清空当前用户信息
                Toast.makeText(ByTheWayActivity.this, "退出登陆成功", Toast.LENGTH_SHORT).show();
                showPasswordDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //单击取消,对话框消失
                dialog.dismiss();
            }
        });
        //创建对话框并且显示
        builder.create().show();
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
