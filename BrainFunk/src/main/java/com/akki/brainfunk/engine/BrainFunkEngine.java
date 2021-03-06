package com.akki.brainfunk.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The {@link BrainFunkEngine} class is an implementation of the original brainfuck
 * language.
 *
 * @author Akhand.
 */
public class BrainFunkEngine implements ScriptEngine {

    /**
     * The memory thats available for this brainfuck program.
     */
    protected byte[] data;

    /**
     * The data pointer that points to the current index in the memory array.
     */
    protected int dataPointer = 0;

    /**
     * The character pointer that points to the current index of the character array
     * of value of its file or string.
     */
    protected int charPointer = 0;

    /**
     * The number of cycle this program has gone through.
     */
    protected long instructionCycles = 0;

    /**
     * The fileReader allows use to read from a file if one is specified.
     */
    protected BufferedReader fileReader;

    /**
     * The streamReader allows us to read from any Stream for the ',' keyword.
     */
    protected InputStreamReader streamReader;

    /**
     * The outWriter allows us to write to the output stream.
     */
    protected OutputStream outWriter;

    /**
     * The current line the engine is at.
     */
    protected int lineCount = 0;

    /**
     * The current column the engine is at.
     */
    protected int columnCount = 0;

    /**
     * The {@link Token} class contains tokens in brainfuck.
     *
     * @author Fabian M.
     */
    protected static class Token {
        public final static char NEXT = '>';
        public final static char PREVIOUS = '<';
        public final static char PLUS = '+';
        public final static char MINUS = '-';
        public final static char OUTPUT = '.';
        public final static char INPUT = ',';
        public final static char BRACKET_LEFT = '[';
        public final static char BRACKET_RIGHT = ']';
    }

    /**
     * Constructs a new {@link BrainFunkEngine} instance.
     *
     * @param cells The amount of memory cells.
     */
    public BrainFunkEngine(int cells) {
        this(cells, new PrintStream(System.out), System.in);
    }

    /**
     * Constructs a new {@link BrainFunkEngine} instance.
     *
     * @param cells The amount of memory cells.
     * @param out   The outputstream of this program.
     */
    public BrainFunkEngine(int cells, OutputStream out) {
        this(cells, out, System.in);
    }

    /**
     * Constructs a new {@link BrainFunkEngine} instance.
     *
     * @param cells The amount of memory cells.
     * @param out   The printstream of this program.
     * @param in    The outputstream of this program.
     */
    public BrainFunkEngine(int cells, OutputStream out, InputStream in) {
        initate(cells);
        outWriter = out;
        streamReader = new InputStreamReader(in);
    }

    /**
     * Initiate this instance.
     */
    protected void initate(int cells) {
        data = new byte[cells];
        dataPointer = 0;
        charPointer = 0;
    }

    /**
     * Interprets the given file.
     *
     * @param file The file to interpret.
     * @throws Exception
     */
    public void interpret(File file) throws Exception {
        fileReader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = fileReader.readLine()) != null) {
            content += line;
            lineCount++;
        }
        interpret(content);
    }

    /**
     * Interprets the given string.
     *
     * @param str The string to interpret.
     * @throws Exception
     */
    public void interpret(String str) throws Exception {
        for (; charPointer < str.length(); charPointer++) {
            interpret(str.charAt(charPointer), str.toCharArray());
            instructionCycles++;
            if (instructionCycles > MAX_INSTRUCTION_CYCLE) {
                throw new IllegalStateException("More cycles used than permitted");
            }
        }
        initate(data.length);
    }

    /**
     * Interprets the given char
     *
     * @param c The char to interpret.
     * @throws Exception
     */
    protected void interpret(char c, char[] chars) throws Exception {
        switch (c) {
            case Token.NEXT:
                // Increment the data pointer (to point to the next cell to the right).
                if ((dataPointer + 1) >= data.length) {
                    // if the pointer reaches the far right, loop back
                    dataPointer = -1;
                }
                dataPointer++;
                break;
            case Token.PREVIOUS:
                // Decrement the data pointer (to point to the next cell to the left).
                if ((dataPointer - 1) < 0) {
                    // if data pointer reaches far left, loop back
                    dataPointer = data.length;
                }
                dataPointer--;
                break;
            case Token.PLUS:
                // Increment (increase by one) the byte at the data pointer.
            /*if ((((int) data[dataPointer]) + 1) > Integer.MAX_VALUE) {
				throw new Exception("Error on line " + lineCount + ", column " + columnCount + ":" 
						+ "byte value at data pointer ("
						+ dataPointer + ") " + " on postion " + charPointer
						+ " higher than byte max value.");
			}*/
                data[dataPointer]++;
                break;
            case Token.MINUS:
                // Decrement (decrease by one) the byte at the data pointer.
			/*if ((data[dataPointer] - 1) < 0) {
				throw new Exception("Error on line " + lineCount + ", column " + columnCount + ":" 
						+ "at data pointer " + dataPointer
						+ " on postion " + charPointer
						+ ": Value can not be lower than zero.");
			}*/
                data[dataPointer]--;
                break;
            case Token.OUTPUT:
                // Output the byte at the current index in a character.
                outWriter.write((char) data[dataPointer]);
                break;
            case Token.INPUT:
                // Accept one byte of input, storing its value in the byte at the data pointer.
                data[dataPointer] = (byte) streamReader.read();
                break;
            case Token.BRACKET_LEFT:
                if (data[dataPointer] == 0) {
                    int i = 1;
                    while (i > 0) {
                        char c2 = chars[++charPointer];
                        if (c2 == Token.BRACKET_LEFT)
                            i++;
                        else if (c2 == Token.BRACKET_RIGHT)
                            i--;
                    }
                }
                break;
            case Token.BRACKET_RIGHT:
                int i = 1;
                while (i > 0) {
                    char c2 = chars[--charPointer];
                    if (c2 == Token.BRACKET_LEFT)
                        i--;
                    else if (c2 == Token.BRACKET_RIGHT)
                        i++;
                }
                charPointer--;
                break;
        }
        columnCount++;
    }
}