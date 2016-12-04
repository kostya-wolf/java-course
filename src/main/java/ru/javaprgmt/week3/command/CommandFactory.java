package ru.javaprgmt.week3.command;

public class CommandFactory {

    public Command createCommand(String[] args) throws Exception{
        switch (args[0]) {
            case "add":
            {
                return new AddCommand(args);
            }
            case "delete": {
                return new DeleteCommand(args);
            }
            case "list": {
                return new ListCommand(args);
            }
            default:
                throw new Exception("Доступные команды: list, add, delete");
        }
    }
}

