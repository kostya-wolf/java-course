package ru.javaprgmt.week3.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;

import static ru.javaprgmt.week3.command.Command.requiredParameters;

public class Printer extends Device {
    public boolean network;
//    MountingType mountingType;

//    private static enum MountingType {
//        PORTABLE,
//        TABLETOP,
//        STATIONARY
//    }


    @Override
    public void save(PrintStream printStream) {
        printStream.println('p');
        super.save(printStream);
        printStream.println(this.network);
    }

    @Override
    public Printer load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        this.network = Boolean.parseBoolean(reader.readLine());
        return this;
    }


    @Override
    void setParams() throws Exception {
        super.setParams();
        String network = requiredParameters.get("--network");
        if ("true".equals(network) || "false".equals(network)){
            this.network = Boolean.parseBoolean(network);
        }
        else {
            throw new Exception("Значение параметра --network указано некорректно");
        }
    }
}
