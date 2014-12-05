package ru.fizteh.fivt.students.Gotovchic;

public abstract class Command {
    static Boolean flag = false; // Indicates if work table is chosen.
    void errorFunction() {
        System.err.println("incorrect syntax");
    }
}