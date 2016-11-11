package ru.javaprgmt.week3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;

abstract class Device {
    int sku;
    String name;
    Date date;
    boolean color;
    int quantity;

    void save(PrintStream printStream){
        printStream.println(this.sku);
        printStream.println(this.name);
        printStream.println(AccountingDevices.format.format(this.date));
        printStream.println(this.color);
        printStream.println(this.quantity);
    }

    Device load(BufferedReader reader) throws IOException, ParseException {
        this.sku = Integer.parseInt(reader.readLine());
        this.name = reader.readLine();
        this.date = AccountingDevices.format.parse(reader.readLine());
        this.color = Boolean.parseBoolean(reader.readLine());
        this.quantity = Integer.parseInt(reader.readLine());
        return this;
    }
}
