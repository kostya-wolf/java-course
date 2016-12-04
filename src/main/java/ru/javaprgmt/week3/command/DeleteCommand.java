package ru.javaprgmt.week3.command;

import ru.javaprgmt.week3.database.DBDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DeleteCommand extends Command {
    DeleteCommand(String[] args) {
        super(args);
    }

    @Override
    public void execute(DBDevice db) throws Exception {
        if (args.length == 1){
            try {
                System.out.println("Не указан инвентарный номер, - вы действительно хотите очистить всю БД? (y или yes): ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String yesNo = reader.readLine();
                if ("yes".equals(yesNo) || "y".equals(yesNo)) {
                    db.devices.clear();
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
                    if (db.devices.containsKey(sku)) {
                        db.devices.remove(sku);
                        System.out.println("Устройство (sku : "+sku+") удалено из БД");
                    }
                    else System.out.println("Указнный SKU ("+sku+") не найден в БД");
                }
                else throw new RuntimeException();
            }
            catch (RuntimeException e){
                System.out.println("Введите команду в формате: delete {--sku число}");
            }
        }
        else {
            throw new Exception("Введите команду в формате: delete {--sku число}");
        }
    }
}
