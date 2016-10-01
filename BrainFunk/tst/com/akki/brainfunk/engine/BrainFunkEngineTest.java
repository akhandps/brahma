package com.akki.brainfunk.engine;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Test class for {@link BrainFunkEngine}
 */
public class BrainFunkEngineTest {

    BrainFunkEngine engine;
    ByteArrayOutputStream stream;

    @Test
    public void interpret() throws Exception {
        stream = new ByteArrayOutputStream();
        engine = new BrainFunkEngine(4,stream);

        String input = " ++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.";
        engine.interpret(input);
        String output = new String(stream.toByteArray(),"UTF-8");
        System.out.println(output);
        Assert.assertEquals("Hello World!", output.trim());
    }

}