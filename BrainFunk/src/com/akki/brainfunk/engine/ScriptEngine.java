package com.akki.brainfunk.engine;

import java.io.File;

/**
 * Interface for any script interpreter engine.
 */
public interface ScriptEngine {

    final static long MAX_INSTRUCTION_CYCLE = 1000000;

    void interpret(File file) throws Exception;

    void interpret(String str) throws Exception;
}
