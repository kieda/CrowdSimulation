package edu.cmu.cs.graphics.crowdsim.module;

import edu.cmu.cs.zkieda.modlang.linker.LinkingException;

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
