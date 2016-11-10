package ru.javaprgmt.week3;

import java.io.OutputStream;
import java.util.Date;

public abstract class Device {
    int sku;
    String name;
    Date date;
    boolean color;

    abstract void save(OutputStream outputStream);
}
