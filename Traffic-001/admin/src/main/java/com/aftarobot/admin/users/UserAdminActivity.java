package com.aftarobot.admin.users;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.UserDTO;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UserAdminActivity extends AppCompatActivity implements UserContract.View {

    Toolbar toolbar;
    TextInputEditText editFirst, editLast, editEmail;
    UserDTO user;
    Spinner spinner;
    Button btnSend;
    DepartmentDTO department;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Administration");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        department = (DepartmentDTO)getIntent().getSerializableExtra("department");
        getSupportActionBar().setSubtitle(department.getDepartmentName());
        userPresenter = new UserPresenter(this);


        setup();
    }
    private void setup() {
        spinner = (Spinner) findViewById(R.id.spinner);
        editFirst = (TextInputEditText)findViewById(R.id.editFirstName);
        editLast = (TextInputEditText)findViewById(R.id.editLastName);
        editEmail = (TextInputEditText)findViewById(R.id.editEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        setSpinner();
    }
    private void confirm() {
        if (TextUtils.isEmpty(editFirst.getText())) {
            Toasty.error(this,"Enter first name",Toast.LENGTH_SHORT,true).show();
            editFirst.setError("Enter first name");
            return;
        }
        if (TextUtils.isEmpty(editLast.getText())) {
            Toasty.error(this,"Enter surname",Toast.LENGTH_SHORT,true).show();
            editLast.setError("Enter surname");
            return;
        }
        if (TextUtils.isEmpty(editEmail.getText())) {
            Toasty.error(this,"Enter emaile",Toast.LENGTH_SHORT,true).show();
            editEmail.setError("Enter email");
            return;
        }
        if (userType == 0) {
            Toasty.warning(this,"Select user type",Toast.LENGTH_SHORT, true).show();
            return;
        }
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Confirmation")
                .setMessage("Do you want to send this user data?\n\n"
                        .concat(editFirst.getText().toString()).concat(" ")
                        .concat(editLast.getText().toString()))
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
        user = new UserDTO();
        user.setUserType(userType);
        switch (userType) {
            case UserDTO.TRAFFIC_OFFICER:
                user.setUserDescription(UserDTO.DESC_TRAFFIC_OFFICER);
                break;
            case UserDTO.ADMINISTRATOR:
                user.setUserDescription(UserDTO.DESC_ADMINISTRATOR);
                break;
            case UserDTO.POLITICAL_OFFICIAL:
                user.setUserDescription(UserDTO.DESC_POLITICAL_OFFICIAL);
                break;
        }
        user.setFirstName(editFirst.getText().toString());
        user.setLastName(editLast.getText().toString());
        user.setEmail(editEmail.getText().toString());
        user.setDepartmentName(department.getDepartmentName());
        user.setDepartmentID(department.getDepartmentID());

        showSnackBar("Sending user data ...","OK","yellow");
        userPresenter.addUser(user);
    }
    int userType;
    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select User Type");
        list.add(UserDTO.DESC_TRAFFIC_OFFICER);
        list.add(UserDTO.DESC_ADMINISTRATOR);
        list.add(UserDTO.DESC_POLITICAL_OFFICIAL);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    userType = 0;
                    return;
                }
                int index = i - 1;
                switch (index) {
                    case 0:
                        userType = UserDTO.TRAFFIC_OFFICER;
                        break;
                    case 1:
                        userType = UserDTO.ADMINISTRATOR;
                        break;
                    case 2:
                        userType = UserDTO.POLITICAL_OFFICIAL;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onUserAdded(String key) {
        snackbar.dismiss();
        showSnackBar("User added","OK","green");
        Toasty.success(this,"User has been added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsersFound(List<UserDTO> users) {

    }

    @Override
    public void onError(String message) {

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
