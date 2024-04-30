/**
 * This class is used by the operating system to interact with the hardware 'FlashMemoryDevice'.
 */
public class DeviceDriver {

    public static final int READ_REPEAT_NUMBER = 5;
    public static final byte READ_FAIL_VALUE = -1;

    FlashMemoryDevice hardware;

    public DeviceDriver(FlashMemoryDevice hardware) {
        this.hardware = hardware;
    }

    public byte read(long address) {
        byte result = READ_FAIL_VALUE;
        for(int i=0; i<READ_REPEAT_NUMBER; i++) {
            byte temp = hardware.read(address);
            if(isFirst(i)) {
                result = temp;
                continue;
            }
            if (isDiffByte(temp, result)) {
                throw new CustomReadException("READ FAIL!");
            }
        }
        return result;
    }

    public void write(long address, byte data) {
        byte read_value = read(address);
        if(isDiffByte(read_value, (byte) 0xFF))
            throw new CustomWriteException("WRITE FAIL!");
        hardware.write(address, data);
    }

    private boolean isDiffByte(byte b1, byte b2) {
        return b1 != b2;
    }

    private boolean isFirst(int n) {
        return n == 0;
    }

}