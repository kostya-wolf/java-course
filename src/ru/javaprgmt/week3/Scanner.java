package ru.javaprgmt.week3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;

class Scanner extends Device {
    boolean[] network = new boolean[2]; // "ETHERNET", "WIFI"

    @Override
    void save(PrintStream printStream) {
        printStream.println('s');
        super.save(printStream);
        printStream.println(this.network[0]+","+this.network[1]);
    }

    @Override
    Scanner load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        String[] EW = reader.readLine().split(",");
        this.network[0] = Boolean.parseBoolean(EW[0]);
        this.network[1] = Boolean.parseBoolean(EW[1]);
        return this;
    }
}
