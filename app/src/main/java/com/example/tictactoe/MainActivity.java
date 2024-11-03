package com.example.tictactoe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView message;
    private TextView noliki;
    private TextView krestiki;
    int nolWins = 0; // количество побед ноликов
    int kresWins = 0; // количество побед ноликов
    String winner = ""; // сюда из функции whoWon() возвращается победитель
    boolean player = true; // переменная для переключения между игроками
    String[][] figures = new String[3][3]; // сюда записываются значения imageButtons
    // функция, по клеткам поля определяющая победителя
    String whoWon(String[][] Figures) {
        //проверяются все строки
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(Figures[i][0], "")) continue;
            if ((Objects.equals(Figures[i][0], Figures[i][1])) && (Objects.equals(Figures[i][0], Figures[i][2]))) {
                Log.i(TAG, Figures[i][0]);
                return Figures[i][0];
            }
        }
        //проверяются все столбцы
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(Figures[0][i], "")) continue;
            if ((Objects.equals(Figures[0][i], Figures[1][i])) && (Objects.equals(Figures[0][i], Figures[2][i]))) {
                return Figures[0][i];
            }
        }
        //первая диагональ
        //если в диагонали есть пустой элемент, то она не проверяется
        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            if (Figures[i][i] == "") {
                flag = false;
                break;
            }
        }
        if (flag) {
            if ((Figures[0][0] == Figures[1][1]) && (Figures[0][0] == Figures[2][2])) {
                return Figures[0][0];
            }
        }
        // вторая диагональ
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (Figures[i][2-i] == "") {
                flag = false;
                break;
            }
        }
        if (flag) {
            if ((Figures[0][2] == Figures[1][1]) && (Figures[0][2] == Figures[2][0])) {
                return Figures[0][2];
            }
        }
        // если победитель не был найден возвращается ноль
        return "0";
    }
    // функция, определяющая ничью
    boolean isDraw(String[][] figures) {
        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (figures[i][j] == "") flag = false;
            }
        }
        return flag;
    }
    // диалоговое окно, которое открывается, когда игрок выигрывает один раунд
    private void openSiteDialog(String winner, ImageButton[][] imageButtons, String[][] figures) {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage("Записано!")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // все переменные обнуляются и начинается новый раунд
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                imageButtons[i][j].setImageResource(R.color.btnColor);
                                figures[i][j] = "";
                            }
                        }
                        player = true;
                    }
                })
                .create();
        aboutDialog.setCanceledOnTouchOutside(false);
        aboutDialog.show();
    }
    // диалоговое окно, которое открывается, когда кто-то победил
    private void openWinDialog(String message, ImageButton[][] imageButtons, String[][] figures) {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("Выход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // выход из приложение
                        onDestroy();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Заново", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // новая игра
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                imageButtons[i][j].setImageResource(R.color.btnColor);
                                figures[i][j] = "";
                            }
                        }
                        player = true;
                        nolWins = 0;
                        kresWins = 0;
                        noliki.setText("Игрок 2: " + nolWins);
                        krestiki.setText("Игрок 2: " + kresWins);
                    }
                })
                .create();
        aboutDialog.setCanceledOnTouchOutside(false);
        aboutDialog.show();
    }
    // диалоговое окно, которое открывается, когда пользователь нажимает на кнопки выхода и презапуска раунда
    private void openSureDialog(boolean flag, ImageButton[][] imageButtons, String[][] figures) {
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).setMessage("Уверены?")
                .setNegativeButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (flag) {
                            // выход из приложение
                            onDestroy();
                            System.exit(0);
                        }
                        else {
                            // новый раунд
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    imageButtons[i][j].setImageResource(R.color.btnColor);
                                    figures[i][j] = "";
                                }
                            }
                            player = true;
                        }
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.textView); // чья сейчас очередь
        noliki = findViewById(R.id.textView1); // количество побед ноликов
        krestiki = findViewById(R.id.textView2); // колиечество побед крестиков
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) figures[i][j] = "";
        // клетки, которые предстоит заполнять крестиками или ноликами
        ImageButton[][] imageButtons = new ImageButton[3][3];
        imageButtons[0][0] = findViewById(R.id.imageButton8);
        imageButtons[0][1] = findViewById(R.id.imageButton9);
        imageButtons[0][2] = findViewById(R.id.imageButton);
        imageButtons[1][0] = findViewById(R.id.imageButton10);
        imageButtons[1][1] = findViewById(R.id.imageButton11);
        imageButtons[1][2] = findViewById(R.id.imageButton12);
        imageButtons[2][0] = findViewById(R.id.imageButton13);
        imageButtons[2][1] = findViewById(R.id.imageButton14);
        imageButtons[2][2] = findViewById(R.id.imageButton15);
        // кнопка выхода из приложения
        Button exit = findViewById(R.id.imageButton20);
        View.OnClickListener Exit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSureDialog(true, imageButtons, figures);
            }
        };
        exit.setOnClickListener(Exit);
        // кнопка, чтобы начать игру заново
        Button replay = findViewById(R.id.imageButton21);
        View.OnClickListener Replay = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSureDialog(false, imageButtons, figures);
            }
        };
        replay.setOnClickListener(Replay);
        // обработка нажатий на клетки
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int finalI = i;
                int finalJ = j;
                imageButtons[i][j].setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(View v) {
                        // ход ноликов
                        if (player) {
                            if (figures[finalI][finalJ] == "") {
                                imageButtons[finalI][finalJ].setImageResource(R.drawable.nolik);
                                figures[finalI][finalJ] = "1";
                                message.setText("Очередь: Игрок 2");
                            }
                        }
                        // ход крестиков
                        else {
                            if (figures[finalI][finalJ] == "") {
                                imageButtons[finalI][finalJ].setImageResource(R.drawable.cross);
                                figures[finalI][finalJ] = "2";
                                message.setText("Очередь: Игрок 1");
                            }
                        }
                        // смена игроков
                        player = !player;
                        // определение выигравшего раунд
                        winner = whoWon(figures);
                        if (winner == "1") {
                            nolWins += 1;
                            noliki.setText("Игрок 1: " + nolWins);
                            // определение победителя
                            if (nolWins == 3) {
                                nolWins = 0;
                                player = true;
                                openWinDialog("Победил Игрок 1!", imageButtons, figures);
                            } else openSiteDialog("Выиграл игрок 1!", imageButtons, figures);
                        }
                        else if (winner == "2") {
                            kresWins += 1;
                            krestiki.setText("Игрок 2: " + kresWins);
                            // определение победителя
                            if (kresWins == 3) {
                                kresWins = 0;
                                player = true;
                                openWinDialog("Победил Игрок 2!", imageButtons, figures);
                            } else openSiteDialog("Выиграл игрок 2!", imageButtons, figures);
                        }
                        // определение ничьей
                        else if (isDraw(figures)) openSiteDialog("Ничья!", imageButtons, figures);
                    }
                });
            }
        }
    }
}