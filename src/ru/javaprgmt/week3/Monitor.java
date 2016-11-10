package ru.javaprgmt.week3;

import java.io.OutputStream;
import java.io.PrintStream;

public class Monitor extends Device {
    int size;
    Kind kind;



    static enum Kind {
        TUBE,
        LCD,
        PROJECTOR
    }

    @Override
    void save(OutputStream outputStream) {
        PrintStream printStream = new PrintStream(outputStream);

        printStream.println(this.name);
    }
}
