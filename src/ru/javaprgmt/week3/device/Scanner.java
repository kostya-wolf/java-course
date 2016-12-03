package ru.javaprgmt.week3.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;

import static ru.javaprgmt.week3.command.Command.requiredParameters;

public class Scanner extends Device {
    public boolean[] network = new boolean[2]; // "ETHERNET", "WIFI"

    @Override
    public void save(PrintStream printStream) {
        printStream.println('s');
        super.save(printStream);
        printStream.println(this.network[0]+","+this.network[1]);
    }

    @Override
    public Scanner load(BufferedReader reader) throws IOException, ParseException {
        super.load(reader);
        String[] EW = reader.readLine().split(",");
        this.network[0] = Boolean.parseBoolean(EW[0]);
        this.network[1] = Boolean.parseBoolean(EW[1]);
        return this;
    }

    @Override
    void setParams() throws Exception {
        super.setParams();
        String netw = requiredParameters.get("--network");
        if (netw != null){
            String[] network = netw.split(",");
            for (String flag : network) {
                if ("ETHERNET".equals(flag)) {
                    this.network[0] = true;
                } else if ("WIFI".equals(flag)) {
                    this.network[1] = true;
                } else {
                    throw new Exception("Значение параметра --network указано некорректно");
                }
            }
        }
    }
}
