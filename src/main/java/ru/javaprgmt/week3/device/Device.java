package ru.javaprgmt.week3.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;

import static ru.javaprgmt.week3.command.Command.format;
import static ru.javaprgmt.week3.command.Command.requiredParameters;

abstract public class Device {

    public enum DeviceType {
        MONITOR,
        SCANNER,
        PRINTER
    }

    public int sku;
    public String name;
    public Date date;
    public boolean color;
    public int quantity;

    public void save(PrintStream printStream){
        printStream.println(this.sku);
        printStream.println(this.name);
        printStream.println(format.format(this.date));
        printStream.println(this.color);
        printStream.println(this.quantity);
    }

    public Device load(BufferedReader reader) throws IOException, ParseException {
        this.sku = Integer.parseInt(reader.readLine());
        this.name = reader.readLine();
        this.date = format.parse(reader.readLine());
        this.color = Boolean.parseBoolean(reader.readLine());
        this.quantity = Integer.parseInt(reader.readLine());
        return this;
    }

    void setParams() throws Exception {
        this.name = requiredParameters.get("--name");
        try
        {
            this.sku = Integer.parseInt(requiredParameters.get("--sku"));
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Значение параметра --sku указано некорректно");
        }

        try
        {
            this.date = format.parse(requiredParameters.get("--date"));
        }
        catch (ParseException e)
        {
            throw new Exception("Значение параметра --date указано некорректно");
        }

        String color = requiredParameters.get("--color");
        if ("true".equals(color) || "false".equals(color)){
            this.color = Boolean.parseBoolean(color);
        }
        else {
            throw new Exception("Значение параметра --color указано некорректно");
        }

        try
        {
            this.quantity = Integer.parseInt(requiredParameters.get("--quantity"));
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Значение параметра --quantity указано некорректно");
        }
    }
}
