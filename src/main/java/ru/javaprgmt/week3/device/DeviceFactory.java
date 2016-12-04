package ru.javaprgmt.week3.device;

public class DeviceFactory {

    public Device createDevice(Device.DeviceType deviceType) throws Exception{
        Device dev;
        switch (deviceType) {
            case MONITOR: dev = new Monitor();
            break;
            case PRINTER: dev = new Printer();
            break;
            case SCANNER: dev = new Scanner();
            break;
            default: throw new Exception("Тип устройства задан не верно");
        }
        dev.setParams();
        return dev;
    }
}
