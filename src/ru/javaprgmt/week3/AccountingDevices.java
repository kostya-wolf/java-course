package ru.javaprgmt.week3;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountingDevices {

    static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    private static HashMap<Integer, Device> devices = new HashMap<>();
    private static File dbDevices = new File("dbDevices.txt");

    static {
        try
        {
            if (!dbDevices.exists()) dbDevices.createNewFile();
            load();
        }
        catch (IOException e){
            System.out.println("Не удалось создать файл dbDevices.txt");
        }
    }

    private static HashMap<String, String> requiredParameters = new HashMap<String, String>();
    static {
        requiredParameters.put("--type", null);
        requiredParameters.put("--sku", null);
        requiredParameters.put("--name", null);
        requiredParameters.put("--date", null);
        requiredParameters.put("--color", null);
        requiredParameters.put("--network", null);
        requiredParameters.put("--size", null);
        requiredParameters.put("--kind", null);
    }

    private enum DeviceType {
        MONITOR,
        SCANNER,
        PRINTER
    }

    public static void save() {
        try {
            OutputStream outputStream = new FileOutputStream(dbDevices);
            PrintStream printStream = new PrintStream(outputStream);
            for (Map.Entry<Integer, Device> pair: devices.entrySet())
            {
                Device dev = pair.getValue();
                if (dev instanceof Monitor){
                    ((Monitor)dev).save(printStream);
                }
                else if (dev instanceof Scanner){
                    ((Scanner)dev).save(printStream);
                }
                else if (dev instanceof Printer){
                    ((Printer)dev).save(printStream);
                }
            }
            outputStream.flush();
            outputStream.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Файл не найден");
        }
        catch (IOException e){
            System.out.println("Ошибка записи в файл");
        }
    }

    public static void load() {
        try {
            InputStream inputStream = new FileInputStream(dbDevices);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            Device d;
            while ((s = reader.readLine()) != null){
                switch (s){
                    case "m": {
                            d = new Monitor().load(reader);
                            devices.put(d.sku, d);
                        }
                        break;
                    case "s": {
                            d = new Scanner().load(reader);
                            devices.put(d.sku, d);
                        }
                        break;
                    case "p": {
                            d = new Printer().load(reader);
                            devices.put(d.sku, d);
                        }
                        break;
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Файл не найден");
        }
        catch (IOException e){
            System.out.println("Ошибка в чтении файла");
        }
        catch (ParseException e)
        {
            System.out.println("Значение параметра date в файле некорректно");
            return;
        }
    }


    public static void main(String[] args) {
        try{
            if (args.length % 2 != 0) {
                switch (args[0]) {
                    case "add":
                    {
                        DeviceType deviceType;
                        for (int i=1; i<args.length; i+=2){
                            if (requiredParameters.containsKey(args[i])) requiredParameters.put(args[i],args[i+1]);
                        }

                        try{
                            String type = requiredParameters.get("--type");
                            deviceType = DeviceType.valueOf(type);

                            if (deviceType == DeviceType.MONITOR) {
                                requiredParameters.remove("--network");
                                requiredParameters.remove("--type");
                            }
                            else if (deviceType == DeviceType.PRINTER || deviceType == DeviceType.SCANNER) {
                                requiredParameters.remove("--size");
                                requiredParameters.remove("--kind");
                                requiredParameters.remove("--type");
                            }
                        }
                        catch (IllegalArgumentException e) {
                            System.out.println("Значение параметра --type указано некорректно");
                            return;
                        }
                        catch (NullPointerException e) {
                            System.out.println("Параметр type обязателен");
                            return;
                        }

                        StringBuffer missingParams = new StringBuffer();
                        for (Map.Entry<String,String> pair: requiredParameters.entrySet()) {
                            if (pair.getValue() == null && (!((deviceType == DeviceType.PRINTER) && (pair.getKey().equals("--network"))))) missingParams.append(pair.getKey()).append(',').append(' ');
                        }
                        if (missingParams.length() > 0){
                            System.out.println("Не указаны обязательные параметры: "+missingParams.substring(0, missingParams.length()-2));
                            return;
                        }
                        else {
                            if (deviceType == DeviceType.MONITOR){
                                Monitor monitor = new Monitor();
                                monitor.name = requiredParameters.get("--name");
                                try
                                {
                                    monitor.sku = Integer.parseInt(requiredParameters.get("--sku"));
                                }
                                catch (NumberFormatException e)
                                {
                                    System.out.println("Значение параметра --sku указано некорректно");
                                    return;
                                }

                                try
                                {
                                    monitor.date = format.parse(requiredParameters.get("--date"));
                                }
                                catch (ParseException e)
                                {
                                    System.out.println("Значение параметра --date указано некорректно");
                                    return;
                                }

                                String color = requiredParameters.get("--color");
                                if ("true".equals(color) || "false".equals(color)){
                                    monitor.color = Boolean.parseBoolean(color);
                                }
                                else {
                                    System.out.println("Значение параметра --color указано некорректно");
                                    return;
                                }

                                try
                                {
                                    monitor.size = Integer.parseInt(requiredParameters.get("--size"));
                                }
                                catch (NumberFormatException e)
                                {
                                    System.out.println("Значение параметра --size указано некорректно");
                                    return;
                                }

                                try
                                {
                                    monitor.kind = Monitor.Kind.valueOf(requiredParameters.get("--kind"));
                                }
                                catch (IllegalArgumentException e)
                                {
                                    System.out.println("Значение параметра --kind указано некорректно");
                                    return;
                                }

                                if (devices.containsKey(monitor.sku)) System.out.println("Запись с таким SKU уже существует, перезаписываю");
                                devices.put(monitor.sku, monitor);
                            }
                            else if (deviceType == DeviceType.PRINTER){
                                Printer printer = new Printer();
                                printer.name = requiredParameters.get("--name");
                                try
                                {
                                    printer.sku = Integer.parseInt(requiredParameters.get("--sku"));
                                }
                                catch (NumberFormatException e)
                                {
                                    System.out.println("Значение параметра --sku указано некорректно");
                                    return;
                                }

                                try
                                {
                                    printer.date = format.parse(requiredParameters.get("--date"));
                                }
                                catch (ParseException e)
                                {
                                    System.out.println("Значение параметра --date указано некорректно");
                                    return;
                                }

                                String color = requiredParameters.get("--color");
                                if ("true".equals(color) || "false".equals(color)){
                                    printer.color = Boolean.parseBoolean(color);
                                }
                                else {
                                    System.out.println("Значение параметра --color указано некорректно");
                                    return;
                                }

                                String network = requiredParameters.get("--network");
                                if ("true".equals(network) || "false".equals(network)){
                                    printer.network = Boolean.parseBoolean(network);
                                }
                                else {
                                    System.out.println("Значение параметра --network указано некорректно");
                                    return;
                                }

                                if (devices.containsKey(printer.sku)) System.out.println("Запись с таким SKU уже существует, перезаписываю");
                                devices.put(printer.sku, printer);
                            }
                            else if (deviceType == DeviceType.SCANNER){
                                Scanner scanner = new Scanner();
                                scanner.name = requiredParameters.get("--name");
                                try
                                {
                                    scanner.sku = Integer.parseInt(requiredParameters.get("--sku"));
                                }
                                catch (NumberFormatException e)
                                {
                                    System.out.println("Значение параметра --sku указано некорректно");
                                    return;
                                }

                                try
                                {
                                    scanner.date = format.parse(requiredParameters.get("--date"));
                                }
                                catch (ParseException e)
                                {
                                    System.out.println("Значение параметра --date указано некорректно");
                                    return;
                                }

                                String color = requiredParameters.get("--color");
                                if ("true".equals(color) || "false".equals(color)){
                                    scanner.color = Boolean.parseBoolean(color);
                                }
                                else {
                                    System.out.println("Значение параметра --color указано некорректно");
                                    return;
                                }

                                String netw = requiredParameters.get("--network");
                                if (netw.length() > 0){
                                    String[] network = netw.split(",");
                                    for (int i=0; i<network.length; i++){
                                        if ("ETHERNET".equals(network[i])){
                                            scanner.network[0] = true;
                                        }
                                        else if ("WIFI".equals(network[i])){
                                            scanner.network[1] = true;
                                        }
                                        else {
                                            System.out.println("Значение параметра --network указано некорректно");
                                            return;
                                        }
                                    }
                                }

                                if (devices.containsKey(scanner.sku)) System.out.println("Запись с таким SKU уже существует, перезаписываю");
                                devices.put(scanner.sku, scanner);
                            }
                        }
                    }
                    break;
                    case "delete": {
                        System.out.println(args[0]);
                    }
                    break;
                    case "list": {
                        System.out.println(args[0]);
                    }
                    break;
                    default:
                        System.out.println("Доступные команды: list, add, delete");
                }
            }
            else throw new ArrayIndexOutOfBoundsException();
            save();
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Введите команду в формате: КОМАНДА {--ПАРАМЕТР ЗНАЧЕНИЕ}...");
            return;
        }

    }

}
