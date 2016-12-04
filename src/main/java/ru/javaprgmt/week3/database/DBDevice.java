package ru.javaprgmt.week3.database;

import ru.javaprgmt.week3.device.Device;
import ru.javaprgmt.week3.device.Monitor;
import ru.javaprgmt.week3.device.Printer;
import ru.javaprgmt.week3.device.Scanner;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class DBDevice {
    private static DBDevice database = new DBDevice();
    private File dbDevices = new File("dbDevices.txt");

    public HashMap<Integer, Device> devices = new HashMap<>();

    public static DBDevice getDatabase() {
        return database;
    }

    private DBDevice() {
        try
        {
            if (!dbDevices.exists()) dbDevices.createNewFile();
            load();
        }
        catch (IOException e){
            System.out.println("Не удалось создать файл базы данных" + e);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void save() {
        try {
            OutputStream outputStream = new FileOutputStream(dbDevices);
            PrintStream printStream = new PrintStream(outputStream);
            for (Map.Entry<Integer, Device> pair: devices.entrySet())
            {
                Device dev = pair.getValue();
                dev.save(printStream);
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

    private void load() throws Exception {
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
        }
    }

}
