package com.example.workoutapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class Session implements Parcelable {
    String type;
    ArrayList<Exercise> exerciseArrayList;
    ArrayList<String> dateList;

    public Session(ArrayList<Exercise> exerciseArrayList) {
        this.exerciseArrayList = exerciseArrayList;
        this.dateList = new ArrayList<>();
        dateList.add(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));

        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < exerciseArrayList.size(); i++) {
            if (!stringArrayList.contains(exerciseArrayList.get(i).getType())) {
                stringArrayList.add(exerciseArrayList.get(i).getType());
            }
        }
        switch (stringArrayList.size()) {
            case 0:
                type = "N/A";
                break;
            case 1:
                type = stringArrayList.get(0);
                break;
            case 2:
                type = stringArrayList.get(0) + " and " + stringArrayList.get(1);
                break;
            default:
                type = "Mixed";
                break;
        }
    }

    protected Session(Parcel in) {
        type = in.readString();
        dateList = in.createStringArrayList();
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public ArrayList<String> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public String getType() {
        return type;
    }

    public void setType() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < exerciseArrayList.size(); i++) {
            if (!stringArrayList.contains(exerciseArrayList.get(i).getType())) {
                stringArrayList.add(exerciseArrayList.get(i).getType());
            }
        }

        if (stringArrayList.size() == 0) {
            this.type = "N/A";
        } else if (stringArrayList.size() == 1) {
            this.type = stringArrayList.get(0);
        } else if (stringArrayList.size() == 2) {
            this.type = stringArrayList.get(0) + " and " + stringArrayList.get(1);
        } else {
            this.type = "Mixed";
        }
    }

    public ArrayList<Exercise> getExerciseArrayList() {
        return exerciseArrayList;
    }

    public void setExerciseArrayList(ArrayList<Exercise> exerciseArrayList) {
        this.exerciseArrayList = exerciseArrayList;
    }

    public void addCurrentDate() {
        dateList.add(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    @Override
    public String toString() {
        String string = type + ":";

        if (exerciseArrayList.size() > 0) {
            for (Exercise exercise : exerciseArrayList) {
                string += "\n\t" + exercise;
            }
        }

        string += "\n" + dateList;

        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeStringList(dateList);
    }
}
