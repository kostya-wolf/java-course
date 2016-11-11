package ru.javaprgmt.week3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;

class Printer extends Device {
    boolean network;
//    MountingType mountingType;

//    private static enum MountingType {
//        PORTABLE,
//        TABLETOP,
//        STATIONARY
//    }


    @Override
    void save(PrintStream printStream) {
        printStream.println('p');
        super.save(printStream);
        printStream.println(this.network);
    }

    @Override
    Printer load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        this.network = Boolean.parseBoolean(reader.readLine());
        return this;
    }
}
