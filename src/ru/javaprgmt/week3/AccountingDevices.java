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
            System.out.println("Не удалось создать файл dbDevices.txt" + e);
        }
    }

    private static HashMap<String, String> requiredParameters = new HashMap<String, String>();
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
            System.out.println("Файл не найден" + e);
        }
        catch (IOException e){
            System.out.println("Ошибка записи в файл" + e);
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
            System.out.println("Файл не найден" + e);
        }
        catch (IOException e){
            System.out.println("Ошибка в чтении файла" + e);
        }
        catch (ParseException e)
        {
            System.out.println("Значение параметра date в файле некорректно" + e);
            return;
        }
    }


    public static void main(String[] args) {
        try{
            if (args.length % 2 != 0) {
                switch (args[0]) {
                    case "add":
                    {
                        add(args);
                    }
                    break;
                    case "delete": {
                        delete(args);
                    }
                    break;
                    case "list": {
                        list();
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

    public static void add(String[] args){
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

        StringBuilder missingParams = new StringBuilder();
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
                    monitor.quantity = Integer.parseInt(requiredParameters.get("--quantity"));
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Значение параметра --quantity указано некорректно");
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
                System.out.println("Устройство (sku : "+monitor.sku+") записано в БД");

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

                try
                {
                    printer.quantity = Integer.parseInt(requiredParameters.get("--quantity"));
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Значение параметра --quantity указано некорректно");
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
                System.out.println("Устройство (sku : "+printer.sku+") записано в БД");

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

                try
                {
                    scanner.quantity = Integer.parseInt(requiredParameters.get("--quantity"));
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Значение параметра --quantity указано некорректно");
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
                System.out.println("Устройство (sku : "+scanner.sku+") записано в БД");
            }
        }
    }

    public static void delete(String[] args){
        if (args.length == 1){
            try {
                System.out.println("Не указан инвентарный номер, - вы действительно хотите очистить всю БД? (y или yes): ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String yesNo = reader.readLine();
                if ("yes".equals(yesNo) || "y".equals(yesNo)) {
                    devices.clear();
                    System.out.println("База очищена");
                }
                reader.close();
            }
            catch (IOException e){
                System.out.println("Ошибка ввода/вывода" +e);
            }
        }
        else if (args.length == 3){
            try {
                int sku = Integer.parseInt(args[2]);
                if ("--sku".equals(args[1])){
                    if (devices.containsKey(sku)) {
                        devices.remove(sku);
                        System.out.println("Устройство (sku : "+sku+") удалено из БД");
                    }
                    else System.out.println("Указнный SKU ("+sku+") не найден в БД");
                }
                else throw new RuntimeException();
            }
            catch (RuntimeException e){
                System.out.println("Введите команду в формате: delete {--sku число}");
                return;
            }
        }
        else {
            System.out.println("Введите команду в формате: delete {--sku число}");
        }
    }

    public static void list(){
        if (devices.size() == 0){
            System.out.println("БД не содержит записей");
        }
        else {
            StringBuilder deviceDesr;
            for (Map.Entry<Integer,Device> pair: devices.entrySet()) {
                Device d = pair.getValue();

                String type="";
                String kind="";
                String inch="";


                if (d instanceof Monitor) {
                    type="Монитор";
                    Monitor m = (Monitor)d;
                    if (m.kind == Monitor.Kind.LCD) kind="ЖК,";
                    else if (m.kind == Monitor.Kind.TUBE) kind="ЭЛТ,";
                    else if (m.kind == Monitor.Kind.PROJECTOR) kind="проектор,";

                    int ost10 = m.size % 10;
                    int ost100 = m.size % 100;
                    if ((ost10 == 1) && (ost100 != 11)) inch=m.size+" дюйм";
                    else if ((ost10 >= 2 && ost10 <= 4) && !(ost100 >= 12 && ost100 <= 14)) inch=m.size+" дюйма";
                    else inch=m.size+" дюймов";
                }
                else if (d instanceof Scanner) type="Сканер";
                else if (d instanceof Printer) type="Принтер";

                String items;
                int ost10 = d.quantity % 10;
                int ost100 = d.quantity % 100;
                if ((ost10 == 1) && (ost100 != 11)) items="штука";
                else if ((ost10 >= 2 && ost10 <= 4) && !(ost100 >= 12 && ost100 <= 14)) items="штуки";
                else items="штук";




                deviceDesr = new StringBuilder(); // {ДАТА} {КОЛИЧЕСТВО} {ТИП} {ИМЯ} - {...}
                deviceDesr.append(format.format(d.date)).append(' ').append(d.quantity).append(' ').append(items).append(' ').append(type).append(' ').append(d.name).append(" - ");
                deviceDesr.append(d.color ? "цветной" : "ч/б").append(' ').append(kind).append(' ').append(inch);
                System.out.println(deviceDesr);
            }
        }
    }


}
