import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeviceDriverTest {

    @Mock
    FlashMemoryDevice hardware;

    DeviceDriver driver;

    @BeforeEach
    void setUp() {
        driver = new DeviceDriver(hardware);
    }

    @Test
    public void read_From_Hardware() {
        when(hardware.read(0xFF)).thenReturn((byte) 0);
        byte data = driver.read(0xFF);
        assertEquals(0, data);
        verify(hardware, times(5)).read(0xFF);
    }

    @Test
    public void read_Fail_From_Hardware() {
        when(hardware.read(0xFF))
                .thenReturn((byte) 0)
                .thenReturn((byte) 0)
                .thenReturn((byte) 1)
                .thenReturn((byte) 0)
                .thenReturn((byte) 0);
        assertThrows(CustomReadException.class, () -> {
            byte data = driver.read(0xFF);
        });
        verify(hardware, times(3)).read(0xFF);
    }

    @Test
    public void write_From_Hardware() {
        when(hardware.read(0x01)).thenReturn((byte)0xFF);
        driver.write(0x01, (byte)0x04);
        verify(hardware, times(5)).read(0x01);
        verify(hardware, times(1)).write(0x01, (byte)0x04);
    }

    @Test
    public void write_Fail_From_Hardware() {
        when(hardware.read(0x01)).thenReturn((byte)0x01);
        assertThrows(CustomWriteException.class, () -> {
            driver.write(0x01, (byte)0x04);
        });
        verify(hardware, times(5)).read(0x01);
        verify(hardware, times(0)).write(0x01, (byte)0x04);
    }
}