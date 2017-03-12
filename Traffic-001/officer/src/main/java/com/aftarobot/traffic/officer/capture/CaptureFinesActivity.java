package com.aftarobot.traffic.officer.capture;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aftarobot.traffic.library.api.FineCache;
import com.aftarobot.traffic.library.api.TicketCache;
import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.aftarobot.traffic.library.util.Constants;
import com.aftarobot.traffic.officer.R;
import com.aftarobot.traffic.officer.services.TicketUploadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CaptureFinesActivity extends AppCompatActivity implements FinesContract.View {


    Toolbar toolbar;
    FinesPresenter presenter;
    AutoCompleteTextView autoText;
    List<FineDTO> fines;
    RecyclerView recyclerView;
    private ImageView iconCancel;
    private RelativeLayout topLayout;
    private RelativeLayout lay1;
    private TextView txtCode;
    private TextView txtReg;
    private TextView txtAmount, txtFineCount, txtTotal;
    private RelativeLayout bottomLayout;
    private TextView txtCharge;
    private Button btnRemoveFine, btnClear, btnSave;
    private CardView fineCard;
    boolean cacheNeeded;
    FloatingActionButton fab;
    private TicketDTO ticket;
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = CaptureFinesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Traffic Officer");
        getSupportActionBar().setSubtitle("Driver Detail Capture");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ticket = (TicketDTO) getIntent().getSerializableExtra("ticket");

        setup();

        presenter = new FinesPresenter(this);
        getCachedFines();
    }

    private void getCachedFines() {
        Log.d(TAG, "getCachedFines: ##############################");
        FineCache.getFines(this, new FineCache.ReadListener() {
            @Override
            public void onDataRead(List<FineDTO> list) {
                Log.d(TAG, "onDataRead: found fines in cache: " + list.size());
                fines = list;
                if (fines.isEmpty()) {
                    Log.w(TAG, "onDataRead: cache empty, getting fines");
                    cacheNeeded = true;
                    presenter.getFines();
                } else {
                    // check if fines downloaded in the past 10 days
                    onFinesFound(fines);
                    Collections.sort(fines);
                    FineDTO f = fines.get(0);
                    DateTime dt = new DateTime().minusDays(10);
                    Log.w(TAG, "onDataRead: dt: " + dt.getMillis() + " ### f.cachedate: " + f.getCacheDate());
                    if (f.getCacheDate() < dt.getMillis()) {
                        Log.e(TAG, "onDataRead: cache expired, getting fines");
                        cacheNeeded = true;
                        presenter.getFines();
                    }
                }

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
            }
        });
    }

    private void setup() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        txtFineCount = (TextView) findViewById(R.id.txtFineCount);
        txtTotal = (TextView) findViewById(R.id.txtFineTotal);
        fineCard = (CardView) findViewById(R.id.card1);
        fineCard.setVisibility(View.GONE);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSaveFine);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        autoText = (AutoCompleteTextView) findViewById(R.id.autoText);
        iconCancel = (ImageView) findViewById(R.id.iconCancel);
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        lay1 = (RelativeLayout) findViewById(R.id.lay1);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtReg = (TextView) findViewById(R.id.txtReg);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        txtCharge = (TextView) findViewById(R.id.txtCharge);
        btnRemoveFine = (Button) findViewById(R.id.btnAddFine);
        autoText.setVisibility(View.GONE);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoText.setText("");
            }
        });
        iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOut(fineCard);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOut(fineCard);
                selectedFines.add(0, fine);
                finesItemAdapter.notifyDataSetChanged();
                setTotals();
            }
        });

        setList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    private void confirm() {
        StringBuilder sb = new StringBuilder();
        sb.append("Driver: ").append(ticket.getFirstName())
                .append(" ").append(ticket.getLastName()).append("\n\n");
        double total = 0;
        for (FineDTO f: selectedFines) {
            total += f.getFine();
            sb.append(f.getCode()).append(" ").append(f.getCode())
                    .append(" - R").append(f.getFine()).append("\n");
        }
        sb.append("\nTotal Amount: ").append(" R").append(total)
                .append("\n\n");
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Ticket Confirmation")
                .setMessage("Would you like complete the process of issuing the ticket?\n\n"
                        .concat(sb.toString()))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadTicket();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void uploadTicket() {
        if (ticket.getFines() == null) {
            ticket.setFines(new HashMap<String, FineDTO>());
        }
        showSnackBar("Traffic fine has been processed. Thank you", "OK", Constants.GREEN);
        for (FineDTO f : selectedFines) {
            ticket.getFines().put(UUID.randomUUID().toString(), f);
        }
        Log.d(TAG, "uploadTicket, about to cache: ".concat(gson.toJson(ticket)));
        TicketCache.addTicket(ticket, this, new TicketCache.WriteListener() {
            @Override
            public void onDataWritten() {
                startTicketService();
                onBackPressed();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void startTicketService() {
        Intent m = new Intent(this, TicketUploadService.class);
        startService(m);
    }

    @Override
    public void onBackPressed() {
        Log.w(TAG, "onBackPressed: **************************" );
        if (!ticket.getFines().isEmpty()) {
            Intent m = new Intent();
            m.putExtra("ticket", ticket);
            setResult(RESULT_OK, m);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onFinesFound(List<FineDTO> list) {
        Log.i(TAG, "onFinesFound: " + list.size());
        this.fines = list;
        autoText.setVisibility(View.VISIBLE);
        final FinesFilterAdapter adapter = new FinesFilterAdapter(this, fines);
        autoText.setThreshold(1);
        autoText.setAdapter(adapter);
        autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
                //fine = fines.get((int) l);
                filteredFines = adapter.getFilteredFines();
                fine = filteredFines.get(i);
                Log.w(TAG, "onItemClick id: " + i + " " + fine.getCode());
                autoText.setText("");
//                Log.d(TAG, "onItemClick: autoText: ".concat(autoText.getText().toString()));
                setCard();
            }
        });
        if (cacheNeeded) {
            FineCache.addFines(fines, this, null);
        }

    }

    FineDTO fine;
    List<FineDTO> selectedFines = new ArrayList<>(),
            filteredFines = new ArrayList<>();
    FinesItemAdapter finesItemAdapter;

    private void setList() {
        finesItemAdapter = new FinesItemAdapter(selectedFines, this, new FinesItemAdapter.FinesItemListener() {
            @Override
            public void onRemoveFineRequested(FineDTO f) {
                confirmDeletion(f);
            }
        });
        recyclerView.setAdapter(finesItemAdapter);
        setTotals();
    }

    private void confirmDeletion(final FineDTO fine) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        StringBuilder sb = new StringBuilder();
        sb.append("CODE: ").append(fine.getCode()).append(" - ");
        if (fine.getRegulation().length() > 1) {
            sb.append(fine.getRegulation());
        } else {
            sb.append(fine.getSection());
        }
        sb.append("  R").append(df.format(fine.getFine()));
        b.setTitle("Confirmation")
                .setMessage("Do you really want to remove this fine from the ticket?\n\n".concat(sb.toString()))
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeSelectedFine(fine);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void setCard() {
        txtCode.setText(fine.getCode());
        txtReg.setText(fine.getRegulation());
        if (fine.getSection().length() > 1) {
            txtReg.setText(fine.getSection());
        }
        txtAmount.setText(df.format(fine.getFine()));
        txtCharge.setText(fine.getCharge());

        iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOut(fineCard);
            }
        });
        iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOut(fineCard);
            }
        });

        animateIn(fineCard);
    }

    private void removeSelectedFine(FineDTO fine) {
        int index = 0;
        boolean isFound = false;
        for (FineDTO f : selectedFines) {
            if (f.getFineID().equalsIgnoreCase(fine.getFineID())) {
                isFound = true;
                break;
            }
            index++;
        }
        if (isFound) {
            selectedFines.remove(index);
            finesItemAdapter.notifyDataSetChanged();
            setTotals();
        }
    }

    private void setTotals() {
        double total = 0;
        int count = selectedFines.size();
        for (FineDTO f : selectedFines) {
            total += f.getFine();
        }
        txtFineCount.setText(String.valueOf(count));
        txtTotal.setText(df.format(total));
    }

    @Override
    public void onError(String message) {

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoText.getWindowToken(), 0);
    }

    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");

    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.2f);
        an.setDuration(300);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();

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
