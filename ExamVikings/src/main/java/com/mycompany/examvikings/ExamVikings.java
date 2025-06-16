package com.mycompany.examvikings;

import com.mycompany.examvikings.GUI.FirstFrame;

/**
 * Основной класс приложения "Пророчество Викингов".
 * Содержит точку входа и глобальные данные .
 */
public class ExamVikings {

    /**
     * Точка входа в приложение.
     * Создаёт начальное окно интерфейса {@link FirstFrame}.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        FirstFrame ff = new FirstFrame();    
    }
}