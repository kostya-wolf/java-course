package ru.javaprgmt.week3.command;

import ru.javaprgmt.week3.database.DBDevice;
import ru.javaprgmt.week3.device.*;

import java.util.Map;

class AddCommand extends Command {
    AddCommand(String[] args) {
        super(args);
    }

    @Override
    public void execute(DBDevice db) throws Exception {
        Device.DeviceType deviceType;
        for (int i=1; i<args.length; i+=2){
            if (requiredParameters.containsKey(args[i])) requiredParameters.put(args[i],args[i+1]);
        }

        try{
            String type = requiredParameters.get("--type");
            deviceType = Device.DeviceType.valueOf(type);

            if (deviceType == Device.DeviceType.MONITOR) {
                requiredParameters.remove("--network");
                requiredParameters.remove("--type");
            }
            else if (deviceType == Device.DeviceType.PRINTER || deviceType == Device.DeviceType.SCANNER) {
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
            System.out.println("Параметр --type обязателен");
            return;
        }

        StringBuilder missingParams = new StringBuilder();
        for (Map.Entry<String,String> pair: requiredParameters.entrySet()) {
            if (pair.getValue() == null && (!((deviceType == Device.DeviceType.SCANNER) && (pair.getKey().equals("--network"))))) missingParams.append(pair.getKey()).append(',').append(' ');
        }
        if (missingParams.length() > 0){
            System.out.println("Не указаны обязательные параметры: "+missingParams.substring(0, missingParams.length()-2));
        }
        else {
            DeviceFactory deviceFactory = new DeviceFactory();
            Device device = deviceFactory.createDevice(deviceType);

            if (db.devices.containsKey(device.sku)) System.out.println("Запись с таким SKU уже существует, перезаписываю");
            db.devices.put(device.sku, device);
            System.out.println("Устройство (sku : "+device.sku+") записано в БД");
        }
    }

}
