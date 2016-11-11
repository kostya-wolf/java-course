package ru.javaprgmt.week3;

import java.io.*;
import java.text.ParseException;

class Monitor extends Device {
    int size;
    Kind kind;

    enum Kind {
        TUBE,
        LCD,
        PROJECTOR
    }

    @Override
    void save(PrintStream printStream) {
        printStream.println('m');
        super.save(printStream);
        printStream.println(this.size);
        printStream.println(this.kind);
    }

    @Override
    Monitor load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        this.size = Integer.parseInt(reader.readLine());
        this.kind = Kind.valueOf(reader.readLine());
        return this;
    }
}
