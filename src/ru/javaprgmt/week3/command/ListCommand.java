package ru.javaprgmt.week3.command;

import ru.javaprgmt.week3.database.DBDevice;
import ru.javaprgmt.week3.device.Device;
import ru.javaprgmt.week3.device.Monitor;
import ru.javaprgmt.week3.device.Printer;

import java.util.Map;

class ListCommand extends Command {
    ListCommand(String[] args) {
        super(args);
    }

    private String findUnit(int number, boolean inches){
        int ost10 = number % 10;
        int ost100 = number % 100;
        if ((ost10 == 1) && (ost100 != 11)) return inches ? "дюйм" : "штука";
        else if ((ost10 >= 2 && ost10 <= 4) && !(ost100 >= 12 && ost100 <= 14)) return inches ? "дюйма" : "штуки";
        else return inches ? "дюймов" : "штук";
    }

    @Override
    public void execute(DBDevice db) throws Exception {
        if (db.devices.size() == 0){
            System.out.println("БД не содержит записей");
        }
        else {
            for (Map.Entry<Integer,Device> pair: db.devices.entrySet()) {

                StringBuilder specifDesc = new StringBuilder();

                Device d = pair.getValue();

                String type="";
                String kind="";

                String items = findUnit(d.quantity, false);

                if (d instanceof Monitor) {
                    type="Монитор";
                    Monitor m = (Monitor)d;
                    if (m.kind == Monitor.Kind.LCD) kind="ЖК,";
                    else if (m.kind == Monitor.Kind.TUBE) kind="ЭЛТ,";
                    else if (m.kind == Monitor.Kind.PROJECTOR) kind="проектор,";

                    String inch = findUnit(m.size, true);

                    specifDesc.append(kind).append(' ').append(m.size).append(' ').append(inch);
                }
                else if (d instanceof ru.javaprgmt.week3.device.Scanner) {
                    type="Сканер";
                    ru.javaprgmt.week3.device.Scanner s = (ru.javaprgmt.week3.device.Scanner)d;
                    if (s.network[0] || s.network[1]) {
                        specifDesc.append("сетевой");
                        if (s.network[0]) specifDesc.append(", с Ethernet");
                        if (s.network[1]) specifDesc.append(", с WiFi");
                    }
                    else specifDesc.append("локальный");
                }
                else if (d instanceof Printer) {
                    type="Принтер";
                    Printer p = (Printer)d;
                    specifDesc.append(p.network ? "сетевой" : "локальный");
                }

                StringBuilder deviceDesc = new StringBuilder();
                deviceDesc.append(format.format(d.date)).append(' ').append(d.quantity).append(' ').append(items).append(' ').append(type).append(' ').append(d.name).append(" - ");
                deviceDesc.append(d.color ? "цветной" : "ч/б").append(' ').append(specifDesc);
                System.out.println(deviceDesc);
            }
        }
    }
}
