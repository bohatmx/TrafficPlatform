package com.aftarobot.admin.departments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.util.Constants;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DepartmentAdminActivity extends AppCompatActivity implements DeptListContract.View {

    Toolbar toolbar;
    TextInputEditText editName, editCity, editEmail;
    DepartmentDTO department;
    List<DepartmentDTO> departments = new ArrayList<>();
    DepartmentPresenter presenter;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Department Administration");
        getSupportActionBar().setSubtitle("Acme Inc.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new DepartmentPresenter(this);
        setup();
    }

    private void setup() {
        editName = (TextInputEditText) findViewById(R.id.editName);
        editCity = (TextInputEditText) findViewById(R.id.editCity);
        editEmail = (TextInputEditText) findViewById(R.id.editEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    private void confirm() {
        if (TextUtils.isEmpty(editName.getText())) {
            Toasty.warning(this,"Name is empty").show();
            editName.setError("Enter name");
            return;
        }
        if (TextUtils.isEmpty(editEmail.getText())) {
            Toasty.warning(this,"Email is empty").show();
            editEmail.setError("Enter email");
            return;
        }
        if (TextUtils.isEmpty(editCity.getText())) {
            Toasty.warning(this,"City Name is empty").show();
            editCity.setError("Enter city name");
            return;
        }
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Confirmation")
                .setMessage("Do you want to send this user data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        send();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void send() {
        department = new DepartmentDTO();
        department.setDepartmentName(editName.getText().toString());
        department.setEmail(editEmail.getText().toString());
        department.setCityName(editCity.getText().toString());

        showSnackBar("Sending department to database", "OK", Constants.CYAN);
        presenter.addDepartment(department);
    }

    @Override
    public void onDepartmentAdded(String key) {
        Log.w("DepartmentAdminActivity", "onDepartmentAdded: ".concat(key) );
        snackbar.dismiss();
        deptAdded = true;
        departments.add(department);
        Toasty.success(this,"Department added to database", Toast.LENGTH_LONG, true).show();


    }

    private boolean deptAdded;

    @Override
    public void onBackPressed() {
        if (deptAdded) {
            Intent m = new Intent();
            ResponseBag bag = new ResponseBag();
            bag.setDepartments(departments);
            m.putExtra("bag", bag);
            setResult(RESULT_OK, m);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();

    }

    @Override
    public void onDepartmentsFound(List<DepartmentDTO> departments) {

    }

    @Override
    public void onError(String message) {
         showSnackBar(message,"bad", Constants.RED);
    }

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
