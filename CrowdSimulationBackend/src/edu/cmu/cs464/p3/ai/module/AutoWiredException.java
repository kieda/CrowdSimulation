package edu.cmu.cs464.p3.ai.module;

import edu.cmu.cs464.p3.modulelang.linker.LinkingException;

/**
 * @author zkieda
 */
public class AutoWiredException extends LinkingException {

    public AutoWiredException() {
    }

    public AutoWiredException(String message) {
        super(message);
    }

    public AutoWiredException(Throwable cause) {
        super(cause);
    }

    public AutoWiredException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
