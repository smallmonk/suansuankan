package com.proxedure.suansuankan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.proxedure.suansuankan.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static final int TOTAL_NUMBER = 5;

    private final int[] numberArray = new int[TOTAL_NUMBER];
    private final ArrayList<EditText> editTextArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        editTextArray.add(binding.box1);
        editTextArray.add(binding.box2);
        editTextArray.add(binding.box3);
        editTextArray.add(binding.box4);
        editTextArray.add(binding.box5);

        initListeners();
        newQuiz();
    }

    private void initListeners() {
        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = checkAnswer();
                showResultDialog(isCorrect);
            }
        });

    }

    private void newQuiz() {
        if (numberArray.length != editTextArray.size()) {
            return;
        }

        Random rand = new Random();

        final int hide1 = rand.nextInt(5);
        int hide2 = rand.nextInt(4);
        if (hide2 >= hide1) {
            hide2++;
        }

        int start = rand.nextInt(91);
        int diff = 2;
        for (int i=0; i<numberArray.length; i++) {
            numberArray[i] = start + i * diff;

            EditText currentEdit = editTextArray.get(i);
            if (hide1 == i || hide2 == i) {
                editTextArray.get(i).setText("");
                currentEdit.setEnabled(true);
            } else {
                currentEdit.setText(String.valueOf(numberArray[i]));
                currentEdit.setEnabled(false);
            }
        }
    }

    private boolean checkAnswer() {
        if (numberArray.length != editTextArray.size()) {
            return false;
        }

        for (int i=0; i<5; i++) {
            try {
                int val = Integer.parseInt(editTextArray.get(i).getText().toString());

                if (val != numberArray[i]) {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }

        return true;
    }

    private void showResultDialog(final boolean isCorrect) {
        final String dialogMessage;
        final String positiveBtnText;
        if (isCorrect) {
            dialogMessage = getString(R.string.message_correct);
            positiveBtnText = getString(R.string.action_new);
        } else {
            dialogMessage = getString(R.string.message_incorrect);
            positiveBtnText = getString(R.string.action_retry);
        }



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isCorrect) {
                    newQuiz();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.action_exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();
            }
        });
        builder.show();
    }
}
