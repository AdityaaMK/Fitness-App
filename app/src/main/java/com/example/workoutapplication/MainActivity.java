package com.example.workoutapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STRING_KEY = "task list";
    Button createSessionButton;
    ListView listView;
    ImageView changeImageView;

    ArrayList<Session> sessionsList;
    SessionsListAdapter sessionsListAdapter;
    int pos;

    ArrayList<Exercise> currentExercises;
    Session currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentExercises = new ArrayList<>();
        currentSession = new Session(currentExercises);

        changeImageView = findViewById(R.id.id_imageview_change);
        changeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        listView = findViewById(R.id.id_listview);

        loadData();
        Log.d("TAG", "AFTER" + sessionsList.toString());
        updateListView();

        createSessionButton = findViewById(R.id.id_button_create_session);
        createSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("TAG", currentExercises.toString() + "\n" + currentSession.toString());
                Toast.makeText(MainActivity.this, "Add Exercises to your new Session.", Toast.LENGTH_LONG).show();

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_session, null);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                final Spinner nameSpinner = view.findViewById(R.id.id_spinner_name);
                ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.name, android.R.layout.simple_spinner_item);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(nameAdapter);
                nameSpinner.setOnItemSelectedListener(MainActivity.this);

                final Spinner typeSpinner = view.findViewById(R.id.id_spinner_type);
                ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.type, android.R.layout.simple_spinner_item);
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpinner.setAdapter(typeAdapter);
                typeSpinner.setOnItemSelectedListener(MainActivity.this);

                final EditText exerciseSets = view.findViewById(R.id.id_edittext_sets);
                final EditText exerciseReps = view.findViewById(R.id.id_edittext_repetitions);
                final EditText exerciseRest = view.findViewById(R.id.id_edittext_rest);

                final Spinner resTypeSpinner = view.findViewById(R.id.id_spinner_resistance_type);
                ArrayAdapter<CharSequence> typeAdapter2 = ArrayAdapter.createFromResource(MainActivity.this, R.array.resistance_types, android.R.layout.simple_spinner_item);
                typeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                resTypeSpinner.setAdapter(typeAdapter2);
                resTypeSpinner.setOnItemSelectedListener(MainActivity.this);

                final EditText exerciseRes = view.findViewById(R.id.id_edittext_resistance);

                Button addExerciseButton = view.findViewById(R.id.id_button_add_exercise);
                addExerciseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nameSpinner.getSelectedItem().toString().equals("Exercise Name") || typeSpinner.getSelectedItem().toString().equals("Exercise Type") || Integer.parseInt(exerciseSets.getText().toString()) < 1 || Integer.parseInt(exerciseReps.getText().toString()) < 1 || Integer.parseInt(exerciseRest.getText().toString()) < 1) {
                            Toast.makeText(MainActivity.this, "Please fill any empty field(s).", Toast.LENGTH_LONG).show();
                        } else {
                            if (exerciseRes.getText().toString().equals("") && resTypeSpinner.getSelectedItem().toString().equals("Res. Type")) {
                                currentExercises.add(new Exercise(nameSpinner.getSelectedItem().toString(), typeSpinner.getSelectedItem().toString(), Integer.parseInt(exerciseReps.getText().toString()), Integer.parseInt(exerciseSets.getText().toString()), Integer.parseInt(exerciseRest.getText().toString())));
                                currentSession.setExerciseArrayList(currentExercises);
                                currentSession.setType();

                                nameSpinner.setSelection(0);
                                typeSpinner.setSelection(0);
                                resTypeSpinner.setSelection(0);
                                exerciseRes.setText("");
                                exerciseReps.setText("");
                                exerciseSets.setText("");
                                exerciseRest.setText("");

                                //Log.d("TAG", currentExercises.toString() + "\n" + currentSession.toString());
                            } else if (exerciseRes.getText().toString().equals("") || resTypeSpinner.getSelectedItem().toString().equals("Res. Type")) {
                                Toast.makeText(MainActivity.this, "Please fill any empty optional field(s) or leave them blank.", Toast.LENGTH_LONG).show();
                            } else {
                                currentExercises.add(new Exercise(nameSpinner.getSelectedItem().toString(), typeSpinner.getSelectedItem().toString(), resTypeSpinner.getSelectedItem().toString(), Integer.parseInt(exerciseRes.getText().toString()), Integer.parseInt(exerciseReps.getText().toString()), Integer.parseInt(exerciseSets.getText().toString()), Integer.parseInt(exerciseRest.getText().toString())));
                                currentSession.setExerciseArrayList(currentExercises);
                                currentSession.setType();
                                saveData();

                                nameSpinner.setSelection(0);
                                typeSpinner.setSelection(0);
                                resTypeSpinner.setSelection(0);
                                exerciseRes.setText("");
                                exerciseReps.setText("");
                                exerciseSets.setText("");
                                exerciseRest.setText("");

                                //Log.d("TAG", currentExercises.toString() + "\n" + currentSession.toString());
                            }
                        }
                    }
                });

                Button finishSessionButton = view.findViewById(R.id.id_button_finish_session);
                finishSessionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(currentSession.getType().equals("N/A"))) {
                            sessionsList.add(currentSession);

                            currentExercises = new ArrayList<>();
                            currentSession = new Session(currentExercises);

                            dialog.dismiss();

                            Log.d("TAG", "SessionsList BEFORE:\n" + sessionsList);

                            saveData();
                            updateListView();
                        } else {
                            Toast.makeText(MainActivity.this, "Please create an exercise before finishing a session.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    public void openActivity2() {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putParcelableArrayListExtra("Session", sessionsList);
        startActivity(intent);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sessionsList);
        editor.putString(STRING_KEY, json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(STRING_KEY, null);
        Type type = new TypeToken<ArrayList<Session>>() {}.getType();
        sessionsList = gson.fromJson(json, type);

        if (sessionsList == null) {
            sessionsList = new ArrayList<>();
        }
    }

    public void updateListView() {
        sessionsListAdapter = new SessionsListAdapter(this, R.layout.adapter_custom, sessionsList);
        listView.setAdapter(sessionsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class SessionsListAdapter extends ArrayAdapter<Session> {
        Context parentContext;
        ArrayList<Session> list;
        int xmlResource;

        public SessionsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Session> objects) {
            super(context, resource, objects);
            parentContext = context;
            xmlResource = resource;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) parentContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterView = layoutInflater.inflate(xmlResource, null);

            TextView sessionTypeText = adapterView.findViewById(R.id.id_textview_session_type);
            TextView exercisesText = adapterView.findViewById(R.id.id_textview_exercises);
            ImageView deleteButton = adapterView.findViewById(R.id.id_imageview_delete);
            Button completeButton = adapterView.findViewById(R.id.id_button_complete);

            sessionTypeText.setText(sessionsList.get(position).getType());

            String exercisesString = "";
            for (int i = 0; i < sessionsList.get(position).getExerciseArrayList().size(); i++) {
                Exercise exercise = sessionsList.get(position).getExerciseArrayList().get(i);

                exercisesString += exercise.getSets() + " x " + exercise.getRepetitions() + "\t\t" + exercise.getName() + "\t\t\t\t\t\t\t\t\t(" + exercise.getRest() + " s. Rest)\n";

                if (exercise.getResistance() != 0) {
                    exercisesString += "\t\t\t>\t" + exercise.getResistance() + " lb. " + exercise.getResistanceType() + " resistance\n";
                }
                exercisesString += "\t\t\t>\tThis exercise targets the " + exercise.getType() + "\n\n";
            }

            exercisesString += "Added: " + sessionsList.get(position).getDateList().get(0) + "\n";

            if (sessionsList.get(position).getDateList().size() > 1) {
                exercisesString += "Last Completed: " + sessionsList.get(position).getDateList().get(sessionsList.get(position).getDateList().size() - 1) + "\n";
            }
            exercisesText.setText(exercisesString);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionsList.remove(pos);

                    Log.d("TAG", "SessionsList Before:\n" + sessionsList);

                    saveData();
                    notifyDataSetChanged();
                }
            });

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionsList.get(position).addCurrentDate();
                    saveData();
                    updateListView();
                    Log.d("TAG", sessionsList + "");
                }
            });

            return adapterView;
        }
    }
}
