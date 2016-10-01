package engine;

import com.akki.brainfunk.engine.BrainFunkEngine;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Test class for {@link BrainFunkEngine}
 */
public class BrainFunkEngineTest {

    private BrainFunkEngine engine;
    private ByteArrayOutputStream stream;

    @Test
    public void interpret() throws Exception {
        stream = new ByteArrayOutputStream();
        engine = new BrainFunkEngine(5,stream);

        String input = " ++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.";
        engine.interpret(input);
        String output = new String(stream.toByteArray(),"UTF-8");
        System.out.println(output);
        Assert.assertEquals("Hello World!", output.trim());
    }

}