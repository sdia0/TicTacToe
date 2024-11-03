package com.example.tictactoe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView message;
    private TextView xVictories, oVictories;
    int pointsX = 0, pointsO = 0;
    int victoriesX = 0, victoriesO = 0;
    String winner = ""; // сюда из функции whoWon() возвращается победитель
    boolean currentPlayer = true; // переменная для переключения между игроками
    String[][] figures = new String[3][3]; // сюда записываются значения imageButtons
    Button exit, replay;
    ImageButton[][] imageButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Прозрачный статус-бар
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        message = findViewById(R.id.playerTurn); // чья сейчас очередь
        oVictories = findViewById(R.id.victoriesO); // количество побед ноликов
        xVictories = findViewById(R.id.victoriesX); // колиечество побед крестиков
        replay = findViewById(R.id.replay); // кнопка, чтобы начать игру заново
        exit = findViewById(R.id.exit); // кнопка выхода из приложения
        // клетки, которые предстоит заполнять крестиками или ноликами
        imageButtons = new ImageButton[3][3];
        imageButtons[0][0] = findViewById(R.id.ceil00);
        imageButtons[0][1] = findViewById(R.id.ceil01);
        imageButtons[0][2] = findViewById(R.id.ceil02);
        imageButtons[1][0] = findViewById(R.id.ceil10);
        imageButtons[1][1] = findViewById(R.id.ceil11);
        imageButtons[1][2] = findViewById(R.id.ceil12);
        imageButtons[2][0] = findViewById(R.id.ceil20);
        imageButtons[2][1] = findViewById(R.id.ceil21);
        imageButtons[2][2] = findViewById(R.id.ceil22);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replayDialog();
            }
        });

        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) figures[i][j] = "";

        // обработка нажатий на клетки
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int finalI = i;
                int finalJ = j;
                imageButtons[i][j].setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(View v) {
                        // ход ноликов
                        if (currentPlayer) {
                            if (Objects.equals(figures[finalI][finalJ], "")) {
                                imageButtons[finalI][finalJ].setImageResource(R.drawable.nolik);
                                figures[finalI][finalJ] = "o";
                                // определение победителя
                                if (isWinner("o")) {
                                    victoriesO++;
                                    if (victoriesO == 3) endOfGameDialog("Победили Нолики!", true);
                                    else endOfGameDialog("Выиграли Нолики!", false);
                                    oVictories.setText("Нолики: " + victoriesO);
                                }
                                else {
                                    boolean fullField = true;
                                    for (int k = 0; k < 3; k++)
                                        for (int l = 0; l < 3; l++)
                                            if (figures[k][l].isEmpty())
                                                fullField = false;
                                    if (fullField) endOfGameDialog("Ничья!", false);
                                }
                                // смена игроков
                                currentPlayer = !currentPlayer;
                                message.setText("Очередь: Крестики");
                            }
                        }
                        // ход крестиков
                        else {
                            if (Objects.equals(figures[finalI][finalJ], "")) {
                                imageButtons[finalI][finalJ].setImageResource(R.drawable.cross);
                                figures[finalI][finalJ] = "x";
                                // определение победителя
                                if (isWinner("x")) {
                                    victoriesX++;
                                    if (victoriesX == 3) endOfGameDialog("Победили Крестики!", true);
                                    else endOfGameDialog("Выиграли Крестики!", false);
                                    xVictories.setText("Крестики: " + victoriesX);
                                }
                                else {
                                    boolean fullField = true;
                                    for (int k = 0; k < 3; k++)
                                        for (int l = 0; l < 3; l++)
                                            if (figures[k][l].isEmpty())
                                                fullField = false;
                                    if (fullField) endOfGameDialog("Ничья!", false);
                                }
                                // смена игроков
                                currentPlayer = !currentPlayer;
                                message.setText("Очередь: Нолики");
                            }
                        }
                    }
                });
            }
        }
    }
    // функция, по клеткам поля определяющая победителя
    boolean isWinner(String player) {
        //проверяются все строки и столбцы
        for (int i = 0; i < 3; i++) {
            if (figures[i][0].equals(player) && figures[i][1].equals(player) && figures[i][2].equals(player))
                return true;
            if (figures[0][i].equals(player) && figures[1][i].equals(player) && figures[2][i].equals(player))
                return true;
        }
        //первая диагональ
        if (figures[0][0].equals(player) && figures[1][1].equals(player) && figures[2][2].equals(player))
            return true;
        // вторая диагональ
        if (figures[0][2].equals(player) && figures[1][1].equals(player) && figures[2][0].equals(player))
            return true;
        // если победитель не был найден возвращается ноль
        return false;
    }
    // диалоговое окно, которое открывается, когда кто-то выигрывает
    private void endOfGameDialog(String message, boolean isEndOfGame) {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset(isEndOfGame);
                    }
                })
                .create();
        aboutDialog.setCanceledOnTouchOutside(false);
        aboutDialog.show();
    }
    void reset(boolean isEndOfGame) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                figures[i][j] = "";
                imageButtons[i][j].setImageResource(R.color.btnColor);
            }
        }
        pointsO = 0;
        pointsX = 0;
        currentPlayer = true;
        message.setText("Очередь: Нолики");
        if (isEndOfGame) {
            victoriesO = 0;
            victoriesX = 0;
            xVictories.setText("Крестики: ");
            oVictories.setText("Нолики: ");
        }
    }
    // диалоговое окно, которое открывается, когда пользователь нажимает на кнопки выхода и презапуска раунда
    private void replayDialog() {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage("Начать заново...")
                .setNegativeButton("Раунд", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset(false);
                    }
                })
                .setPositiveButton("Игру", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset(true);
                    }
                })
                .create();
        aboutDialog.show();
    }
    private void exitDialog() {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage("Уверены?")
                .setNegativeButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDestroy();
                        System.exit(0);
                    }
                })
                .setPositiveButton("Нет", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        aboutDialog.show();
    }
}