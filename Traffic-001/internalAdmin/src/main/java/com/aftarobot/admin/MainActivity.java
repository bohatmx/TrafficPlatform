package com.aftarobot.admin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.admin.departments.DepartmentAdminActivity;
import com.aftarobot.admin.departments.DepartmentListFragment;
import com.aftarobot.admin.fines.FinesFragment;
import com.aftarobot.admin.users.UserAdminActivity;
import com.aftarobot.admin.users.UserListFragment;
import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.DepthPageTransformer;
import com.aftarobot.traffic.library.util.PagerFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FinesFragment.FinesFragmentListener, UserListFragment.UserFragmentListener,
        DepartmentListFragment.DepartmentrFragmentListener {

    ViewPager viewPager;
    FloatingActionButton fab;
    Toolbar toolbar;
    DrawerLayout drawer;
    UserListFragment userListFragment;
    DepartmentListFragment departmentListFragment;

    FinesFragment finesFragment;
    PagerAdapter pagerAdapter;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Administration");
        getSupportActionBar().setSubtitle("Department Setup Maintenance");

        setup();
        buildPages();
    }

    NavigationView navigationView;

    private void setup() {
        Log.d(TAG, "setup: .......................");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START, true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hdr = navigationView.getHeaderView(0);
        TextView txt = (TextView) hdr.findViewById(R.id.txtName);
        txt.setText("Staff Member");

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void buildPages() {
        pagerFragments = new ArrayList<>(2);
        userListFragment = UserListFragment.newInstance();
        finesFragment = FinesFragment.newInstance();
        departmentListFragment = DepartmentListFragment.newInstance();

        pagerFragments.add(departmentListFragment);
        pagerFragments.add(userListFragment);
        pagerFragments.add(finesFragment);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFineCreated() {

    }

    private static final int REQUEST_NEW_USER = 109, REQUEST_NEW_DEPT = 655;


    @Override
    public void addNewUser(DepartmentDTO dept) {
        Log.d(TAG, "addNewUser: ........");
        Intent m = new Intent(this, UserAdminActivity.class);
        m.putExtra("department", dept);
        startActivityForResult(m, REQUEST_NEW_USER);
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        switch (reqCode) {
            case REQUEST_NEW_USER:

                if (resCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: new user added");
                    showSnackBar("User added to database");
                }
                break;
            case REQUEST_NEW_DEPT:
                if (resCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: new department added");
                    ResponseBag bag = (ResponseBag) data.getSerializableExtra("bag");
                    if (bag != null) {
                        showSnackBar("Departments added to database: " + bag.getDepartments().size());
                        departmentListFragment.addDepartments(bag.getDepartments());
                    }
                }
                break;
        }
    }

    @Override
    public void updateUser(UserDTO user) {

    }

    @Override
    public void deactivateUser(UserDTO user) {

    }

    @Override
    public void addNewDepartment() {
        Intent m = new Intent(this, DepartmentAdminActivity.class);
        startActivityForResult(m, REQUEST_NEW_DEPT);
    }

    @Override
    public void updateDepartment(DepartmentDTO department) {

    }

    @Override
    public void deactivateDepartment(DepartmentDTO department) {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            return (android.support.v4.app.Fragment) pagerFragments.get(i);
        }

        @Override
        public int getCount() {
            return pagerFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

//            return drawTab(pagerFragments.get(position));
            return pagerFragments.get(position).getTitle();
        }
    }

    private CharSequence drawTab(PagerFragment pf) {
        SpannableStringBuilder sb = new SpannableStringBuilder(pf.getTitle()); // space added before text for convenience

        Drawable drawable = ContextCompat.getDrawable(this, com.aftarobot.traffic.library.R.drawable.ic_alarm);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    private List<PagerFragment> pagerFragments;
    Snackbar snackbar;

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void showSnackBar(String title) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
