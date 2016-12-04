package ru.javaprgmt.week3;

import ru.javaprgmt.week3.command.Command;
import ru.javaprgmt.week3.command.CommandFactory;
import ru.javaprgmt.week3.database.DBDevice;


public class AccountingDevices {

    public static void main(String[] args) {
        try{
            DBDevice db = DBDevice.getDatabase();
            if (args.length % 2 != 0) {
                CommandFactory commandFactory = new CommandFactory();
                Command command = commandFactory.createCommand(args);
                command.execute(db);
            }
            else throw new ArrayIndexOutOfBoundsException();
            db.save();
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Введите команду в формате: КОМАНДА {--ПАРАМЕТР значение}...");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
