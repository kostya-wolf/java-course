package ru.javaprgmt.week3.device;

import java.io.*;
import java.text.ParseException;

import static ru.javaprgmt.week3.command.Command.requiredParameters;

public class Monitor extends Device {
    public int size;
    public Kind kind;

    public enum Kind {
        TUBE,
        LCD,
        PROJECTOR
    }

    @Override
    public void save(PrintStream printStream) {
        printStream.println('m');
        super.save(printStream);
        printStream.println(this.size);
        printStream.println(this.kind);
    }

    @Override
    public Monitor load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        this.size = Integer.parseInt(reader.readLine());
        this.kind = Kind.valueOf(reader.readLine());
        return this;
    }

    @Override
    void setParams() throws Exception {
        super.setParams();
        try
        {
            this.size = Integer.parseInt(requiredParameters.get("--size"));
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Значение параметра --size указано некорректно");
        }

        try
        {
            this.kind = Kind.valueOf(requiredParameters.get("--kind"));
        }
        catch (IllegalArgumentException e)
        {
            throw new Exception("Значение параметра --kind указано некорректно");
        }
    }
}
