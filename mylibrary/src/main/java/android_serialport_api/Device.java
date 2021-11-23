package android_serialport_api;

public class Device {

    public static final char NONE = 'n', ODD = 'n', EVEN = 'n';

    private String path = "/dev/ttyMT0";
    private int speed = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private char parity = 'n';   //'n':无校验;  'o':奇校验;  'e':偶校验;
    private boolean block = false;

    public Device() {
    }

    public Device(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public char getParity() {
        return parity;
    }

    public int getParityInt() {
        if (this.parity == 'n'){
            return 0;
        }else if (this.parity == 'o'){
            return 1;
        }else if (this.parity == 'e'){
            return 2;
        }
        return 0;
    }

    public void setParity(char parity) {
        this.parity = parity;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Device{" +
                "path='" + path + '\'' +
                ", speed=" + speed +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                ", block=" + block +
                '}';
    }
}
