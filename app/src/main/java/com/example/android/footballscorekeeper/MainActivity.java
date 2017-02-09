package com.example.android.footballscorekeeper;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> barcelona = new ArrayList<>();
    private ArrayList<String> realMadrid = new ArrayList<>();
    private ArrayList<String> yellowCards = new ArrayList<>();
    private TextView timerTextView;
    private TextView score_TeamA;
    private TextView score_TeamB;
    private TableLayout tableLayout;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private Button goalTeamA;
    private Button goalTeamB;
    private Button yellowTeamA;
    private Button yellowTeamB;
    private Button redTeamA;
    private Button redTeamB;
    private int scoreTeamA;
    private int scoreTeamB;
    private long startTime;
    private long millis;
    private int seconds;
    private int minutes;
    private boolean isAdded = false;
    private boolean hasNotEnoughPlayers = false;
    private int rowCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        timerTextView = (TextView) findViewById(R.id.timer);
        score_TeamA = (TextView) findViewById(R.id.teamA_score);
        score_TeamB = (TextView) findViewById(R.id.teamB_score);
        tableLayout = (TableLayout) findViewById(R.id.myTable);
        goalTeamA = (Button) findViewById(R.id.goalTeamA_button);
        goalTeamB = (Button) findViewById(R.id.goalTeamB_button);
        yellowTeamA = (Button) findViewById(R.id.goalTeamA_yellow_card);
        yellowTeamB = (Button) findViewById(R.id.goalTeamB_yellow_card);
        redTeamA = (Button) findViewById(R.id.goalTeamA_red_card);
        redTeamB = (Button) findViewById(R.id.goalTeamB_red_card);

        // Start timer
        startTimer();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // Load players
        loadBarcelonaPlayers();
        loadRealMadridPlayers();

        // Set scores to 0
        scoreTeamA = 0;
        scoreTeamB = 0;

        rowCounter = 1;

        // Add OnClickListeners for buttons
        addOnClickListeners();
    }

    private void loadBarcelonaPlayers() {
        barcelona.add("ter Stegen");
        barcelona.add("Roberto");
        barcelona.add("Pique");
        barcelona.add("Mascherano");
        barcelona.add("Alba");
        barcelona.add("Rakitic");
        barcelona.add("Busquets");
        barcelona.add("Gomes");
        barcelona.add("Messi");
        barcelona.add("Suarez");
        barcelona.add("Neymar");
    }

    private void loadRealMadridPlayers() {
        realMadrid.add("Navas");
        realMadrid.add("Carvajal");
        realMadrid.add("Varane");
        realMadrid.add("Ramos");
        realMadrid.add("Marcelo");
        realMadrid.add("Modric");
        realMadrid.add("Kovacic");
        realMadrid.add("Lucas");
        realMadrid.add("Isco");
        realMadrid.add("Ronaldo");
        realMadrid.add("Benzema");
    }

    private void startTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000) * 60;
            minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 1000);

            if (minutes == 45 && seconds == 0) {
                if (!isAdded) {
                    addHeader("2nd Half");
                    isAdded = true;
                }
            } else if (minutes == 90) {
                addHeader("");
                timerHandler.removeCallbacks(timerRunnable);
                removeOnClickListeners();
            }
            }
        };
    }

    private String getPlayer(ArrayList listOfPlayers) {
        Random random = new Random();
        int randomNumber = random.nextInt(listOfPlayers.size());

        return (String) listOfPlayers.get(randomNumber);
    }

    private void refreshScore() {
        score_TeamA.setText(scoreTeamA + "");
        score_TeamB.setText(scoreTeamB + "");
    }

    private void addOnClickListeners(){
        goalTeamA.setOnClickListener(new MyOnClickListener());
        goalTeamB.setOnClickListener(new MyOnClickListener());
        yellowTeamA.setOnClickListener(new MyOnClickListener());
        yellowTeamB.setOnClickListener(new MyOnClickListener());
        redTeamA.setOnClickListener(new MyOnClickListener());
        redTeamB.setOnClickListener(new MyOnClickListener());
    }

    private void removeOnClickListeners() {
        goalTeamA.setOnClickListener(null);
        goalTeamB.setOnClickListener(null);
        yellowTeamA.setOnClickListener(null);
        yellowTeamB.setOnClickListener(null);
        redTeamA.setOnClickListener(null);
        redTeamB.setOnClickListener(null);
    }

    private void cleanTable() {
        int childCount = tableLayout.getChildCount();

        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }
    }

    public void reset(View view) {
        barcelona.clear();
        realMadrid.clear();
        yellowCards.clear();
        loadRealMadridPlayers();
        loadBarcelonaPlayers();
        scoreTeamA = 0;
        scoreTeamB = 0;
        refreshScore();
        cleanTable();
        addOnClickListeners();
        isAdded = false;
        hasNotEnoughPlayers = false;
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void addTableRow(String time, int resID, String player, String score, String team) {
        Typeface font = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        int textSize = 15;

        // Creating a new TableRow
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        if (rowCounter % 2 == 0) {
            // Background Color for even rows
            tr.setBackgroundColor(Color.rgb(245, 245, 245));
        } else {
            // Background Color for odd rows
            tr.setBackgroundColor(Color.rgb(238, 238, 238));
        }

        /* Tablerow weights: 9.5 - 3 - 9.5
            - extra space column: 0.5
            - time column: 1.2
            - image column: 1.5
            - player's name column: 6.3
            - score column: 3
            - blank side: 9.5

        */

        // First Column - Extra space
        TextView extra_space = new TextView(this);
        extra_space.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

        // Second Column - Time
        TextView time_tw = new TextView(this);
        time_tw.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2F));
        time_tw.setText("'" + time);
        time_tw.setTypeface(font);
        time_tw.setTextSize(textSize);
        if (team.equals("realmadrid")) {
            time_tw.setGravity(Gravity.RIGHT);
        }

        // Third Column - ImageIcon
        ImageView imageView = new ImageView(this);
        TableRow.LayoutParams par = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5F);
        par.setMargins(0, 15, 0, 0);
        imageView.setImageResource(resID);
        imageView.setLayoutParams(par);

        // Fourth Column - Player's Name
        TextView player_tw = new TextView(this);
        player_tw.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 6.3F));
        player_tw.setText(player);
        player_tw.setTypeface(font);
        player_tw.setTextSize(textSize);
        player_tw.setPadding(0, 7, 0, 7);
        if (team.equals("realmadrid")) {
            player_tw.setGravity(Gravity.RIGHT);
        }

        // Fifth Column - Score
        TextView score_tw = new TextView(this);
        score_tw.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3));
        score_tw.setText(score);
        score_tw.setTypeface(font);
        score_tw.setTextSize(textSize);
        score_tw.setGravity(Gravity.CENTER);

        // Sixth Column - Rest of the row
        TextView blank_tw = new TextView(this);
        blank_tw.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 9.5F));
        blank_tw.setText("");

        switch (team) {
            case "barcelona":
                tr.addView(extra_space);
                tr.addView(time_tw);
                tr.addView(imageView);
                tr.addView(player_tw);
                tr.addView(score_tw);
                tr.addView(blank_tw);
                break;
            case "realmadrid":
                tr.addView(blank_tw);
                tr.addView(score_tw);
                tr.addView(player_tw);
                tr.addView(imageView);
                tr.addView(time_tw);
                tr.addView(extra_space);
                break;
        }
        tableLayout.addView(tr);
        rowCounter++;
    }

    private void addHeader(String header) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow
                .LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        textView.setText(header);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setBackground(getDrawable(R.drawable.gradient));
        }
        textView.setTextAppearance(this, R.style.table_subheaders);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 3, 0, 3);
        textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        textView.setTextSize(16);
        tr.addView(textView);
        tableLayout.addView(tr);
    }

    public void setData(String team, String event) {
        String player = null;
        String scoreString = null;
        Resources res = this.getResources();
        int resID = 0;

        if (team.equals("barcelona")) {
            player = getPlayer(barcelona);

            if (event.equals("goal")) {
                ++scoreTeamA;
                resID = res.getIdentifier("soccer_ball_15", "drawable", this.getPackageName());
                scoreString = scoreTeamA + " - " + scoreTeamB;
            } else if (event.equals("yellow_card")) {
                if (yellowCards.contains(player)) {
                    barcelona.remove(player);
                    resID = res.getIdentifier("yellow_red", "drawable", this.getPackageName());
                } else {
                    yellowCards.add(player);
                    resID = res.getIdentifier("yellow_card", "drawable", this.getPackageName());
                }
                scoreString = "";
            } else if (event.equals("red_card")) {
                barcelona.remove(player);
                resID = res.getIdentifier("red_card", "drawable", this.getPackageName());
                scoreString = "";

                if (barcelona.size() < 7) {
                    timerHandler.removeCallbacks(timerRunnable);
                    removeOnClickListeners();
                    hasNotEnoughPlayers = true;
                }
            }

        } else if (team.equals("realmadrid")) {
            player = getPlayer(realMadrid);

            if (event.equals("goal")) {
                ++scoreTeamB;
                resID = res.getIdentifier("soccer_ball_15", "drawable", this.getPackageName());
                scoreString = scoreTeamA + " - " + scoreTeamB;
            } else if (event.equals("yellow_card")) {
                if (yellowCards.contains(player)) {
                    realMadrid.remove(player);
                    resID = res.getIdentifier("yellow_red", "drawable", this.getPackageName());
                } else {
                    yellowCards.add(player);
                    resID = res.getIdentifier("yellow_card", "drawable", this.getPackageName());
                }
                scoreString = "";
            } else if (event.equals("red_card")) {
                realMadrid.remove(player);
                resID = res.getIdentifier("red_card", "drawable", this.getPackageName());
                scoreString = "";

                if (realMadrid.size() < 7) {
                    timerHandler.removeCallbacks(timerRunnable);
                    removeOnClickListeners();
                    hasNotEnoughPlayers = true;
                }
            }
        }

        addTableRow(String.valueOf(minutes), resID, player, scoreString, team);
        refreshScore();

        if (hasNotEnoughPlayers) {
            addHeader("Game ended!");
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.goalTeamA_button) {
                setData("barcelona", "goal");
            } else if (view.getId() == R.id.goalTeamB_button) {
                setData("realmadrid", "goal");
            } else if (view.getId() == R.id.goalTeamA_yellow_card) {
                setData("barcelona", "yellow_card");
            } else if (view.getId() == R.id.goalTeamB_yellow_card) {
                setData("realmadrid", "yellow_card");
            } else if (view.getId() == R.id.goalTeamA_red_card) {
                setData("barcelona", "red_card");
            } else if (view.getId() == R.id.goalTeamB_red_card) {
                setData("realmadrid", "red_card");
            }
        }
    }
}
