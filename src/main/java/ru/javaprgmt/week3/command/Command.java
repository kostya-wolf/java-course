package ru.javaprgmt.week3.command;

import ru.javaprgmt.week3.database.DBDevice;

import java.text.SimpleDateFormat;
import java.util.HashMap;

abstract public class Command {
    String args[];

    public static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public static HashMap<String, String> requiredParameters = new HashMap<>();
    static {
        requiredParameters.put("--type", null);
        requiredParameters.put("--sku", null);
        requiredParameters.put("--name", null);
        requiredParameters.put("--date", null);
        requiredParameters.put("--color", null);
        requiredParameters.put("--quantity", "1");
        requiredParameters.put("--network", null);
        requiredParameters.put("--size", null);
        requiredParameters.put("--kind", null);
    }

    abstract public void execute(DBDevice db) throws Exception;

    Command(String[] args){
        this.args = args;
    }
}
