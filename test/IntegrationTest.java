import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    public void testEncodeCreatesOutputFile() throws Exception {
        String input = "test_input.png";
        String output = "test_inputEncoded.png";

        Encode encoder = new Encode(input, 4, 5);
        encoder.makeEncoded(4, 5);

        File outFile = new File(output);
        assertTrue(outFile.exists(), "Encoded output file should be created");

        BufferedImage result = ImageIO.read(outFile);
        assertNotNull(result);
        assertEquals(result.getWidth(), result.getHeight()); // sanity check
    }

    @Test
    public void testReColorCreatesOutputFile() throws Exception {
        String input = "test_input.png";
        String output = "test_inputRed.png";

        ReColor recolor = new ReColor(input, 4, 5);
        recolor.makeRed();

        File outFile = new File(output);
        assertTrue(outFile.exists(), "Red-filtered output file should be created");

        BufferedImage result = ImageIO.read(outFile);
        assertNotNull(result, "Red output image should be readable");
    }
}
