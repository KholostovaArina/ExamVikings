package com.mycompany.examvikings;

import com.mycompany.examvikings.GUI.MainFrame;

public class ExamVikings {

    public static void main(String[] args) {
        Cities.loadCitiesFromDB();
        MainFrame.createMainFrame();
    }
}
