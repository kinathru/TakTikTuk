package com.kinath.taktiktuk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String UNSELECTED_TEXT = "*";
    public static final String USER_SELECTED_TEXT = "x";
    public static final String COMPUTER_SELECTED_TEXT = "o";

    Button btnX0, btnX1, btnX2, btnX3, btnX4, btnX5, btnX6, btnX7, btnX8 = null;
    Button[][] tikMatrix = new Button[3][3];

    boolean userClickCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnX0 = findViewById(R.id.btnX0);
        btnX1 = findViewById(R.id.btnX1);
        btnX2 = findViewById(R.id.btnX2);
        btnX3 = findViewById(R.id.btnX3);
        btnX4 = findViewById(R.id.btnX4);
        btnX5 = findViewById(R.id.btnX5);
        btnX6 = findViewById(R.id.btnX6);
        btnX7 = findViewById(R.id.btnX7);
        btnX8 = findViewById(R.id.btnX8);

        tikMatrix[0][0] = btnX0;
        tikMatrix[0][1] = btnX1;
        tikMatrix[0][2] = btnX2;
        tikMatrix[1][0] = btnX3;
        tikMatrix[1][1] = btnX4;
        tikMatrix[1][2] = btnX5;
        tikMatrix[2][0] = btnX6;
        tikMatrix[2][1] = btnX7;
        tikMatrix[2][2] = btnX8;

    }

    public void resetButtons(View view) {
        for (Button[] btnRow : tikMatrix) {
            for (Button btn : btnRow) {
                btn.setText(R.string.unSelected);
                btn.setEnabled(true);
            }
        }
    }

    public void play(View view) {

        Button selectedBtn = findViewById(view.getId());

        if (isGameButton(selectedBtn) && selectedBtn.isEnabled()) {
            markSelected(selectedBtn, true);
            setProgressBarVisibility(true);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!isGameOver(getSelectedPattern())) {
                playComputer();
            }
            int evaluate = evaluate();
            postEvaluation(evaluate);
        }
    }

    private void postEvaluation(int evaluate) {
        if (evaluate == 0) {
            Toast.makeText(this, "Computer Wins", Toast.LENGTH_LONG).show();
        } else if (evaluate == 1) {
            Toast.makeText(this, "Human Wins", Toast.LENGTH_LONG).show();
        } else if (evaluate == 2) {
            Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
            try {
                Log.i("PEVAL", "Game Over! Waiting before reset");
                Thread.sleep(5000);
                Log.i("PEVAL", "Game Over! Wait Complete!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resetButtons(null);
        }
    }

    private int evaluate() {

        int[][] selectedPattern = getSelectedPattern();
        printMatrix(selectedPattern);

        int result = isWon(selectedPattern, 1) ? 1 : isWon(selectedPattern, 0) ? 0 : isGameOver(selectedPattern) ? 2 : 3;

        return result;

    }

    private int[][] getSelectedPattern() {
        int[][] selectedPattern = new int[3][3];
        for (int i = 0; i < tikMatrix.length; i++) {
            for (int j = 0; j < tikMatrix[i].length; j++) {
                Button btn = this.tikMatrix[i][j];
                selectedPattern[i][j] = decodeSelection(btn);
            }
        }
        return selectedPattern;
    }

    private void printMatrix(int[][] selectedPattern) {
        StringBuilder pattern = new StringBuilder();
        pattern.append("\n");
        for (int i = 0; i < selectedPattern.length; i++) {
            for (int j = 0; j < selectedPattern[i].length; j++) {
                pattern.append(selectedPattern[i][j]).append("\t");
            }
            pattern.append("\n");
        }
        Log.i("PATTERN", pattern.toString());
    }

    private boolean isGameOver(int[][] selectedPattern) {
        for (int i = 0; i < selectedPattern.length; i++) {
            for (int j = 0; j < selectedPattern.length; j++) {
                if (selectedPattern[i][j] < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isWon(int[][] selectedPattern, int checkValue) {
        boolean winningPatternFound = false;
        for (int i = 0; i < 3; i++) {
            if ((selectedPattern[i][0] == checkValue && selectedPattern[i][1] == checkValue && selectedPattern[i][2] == checkValue)
                    || (selectedPattern[0][i] == checkValue && selectedPattern[1][i] == checkValue && selectedPattern[2][i] == checkValue)) {
                winningPatternFound = true;
            } else if (i == 0 && (
                    (selectedPattern[i][0] == checkValue && selectedPattern[i + 1][1] == checkValue && selectedPattern[i + 2][2] == checkValue)
                            || (selectedPattern[i][2] == checkValue && selectedPattern[i + 1][1] == checkValue && selectedPattern[i + 2][0] == checkValue)
            )) {
                winningPatternFound = true;
            }

            if (winningPatternFound) {
                break;
            }
        }

        return winningPatternFound;
    }

    private int decodeSelection(Button btn) {
        Log.d("GEN", btn.getText().toString());
        if (UNSELECTED_TEXT.equalsIgnoreCase(btn.getText().toString())) {
            return -1;
        } else if (USER_SELECTED_TEXT.equalsIgnoreCase((btn.getText().toString()))) {
            return 1;
        } else if (COMPUTER_SELECTED_TEXT.equalsIgnoreCase(btn.getText().toString())) {
            return 0;
        }
        return -1;
    }

    public boolean isGameButton(Button button) {
        return UNSELECTED_TEXT.equals(button.getText().toString());
    }

    public void markSelected(Button button, boolean isUser) {
        button.setEnabled(false);
        button.setText(isUser ? R.string.xSelected : R.string.oselected);

        if (isUser) {
            userClickCompleted = true;
        }
    }

    /**
     * Can be designed with strategy pattern where computer player can use different strategies.
     * GamingStrategy
     * - RandomGamingStrategy
     * - AIGamingStrategy
     */
    public void playComputer() {
        randomPlay();
    }

    public void randomPlay() {

        Random random = new Random();
        boolean randomIndexPicked = false;

        while (!randomIndexPicked) {
            int randomIndex = random.nextInt(9);
            int row = randomIndex / 3;
            int column = randomIndex % 3;

            Button randomBtn = this.tikMatrix[row][column];
            if (randomBtn.isEnabled()) {
                markSelected(randomBtn, false);
                randomIndexPicked = true;
            }
        }
    }

    public void test(View view) {
        Button btn = (Button) view;
        new Thread(() -> runOnUiThread(() -> {
            if (btn.getText().toString().equalsIgnoreCase("TEST")) {
                btn.setText("TEST CLICKED");
            } else {
                btn.setText("TEST");
            }
        })).start();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Log.i("WAIT", "Waiting Completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}