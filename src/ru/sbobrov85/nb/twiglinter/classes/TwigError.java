package ru.sbobrov85.nb.twiglinter.classes;

import java.util.Arrays;
import java.util.List;

/**
 * Twig error class.
 */
public class TwigError {
    /**
     * Contains full file name.
     */
    private String file = "";

    /**
     * Contains line number.
     */
    private int line = 1;

    /**
     * Contains error message.
     */
    private String message = "";

    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * @param csvLine comma-separated string (file,line,message).
     */
    public TwigError(final String csvLine) {
        List<String> parts = Arrays.asList(csvLine.split("\\s*,\\s*"));
        if (parts.size() == 3) {
            file = parts.get(0);
            line = Integer.parseInt(parts.get(1));
            message = parts.get(2);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Getter for file property.
     * @return full file name.
     */
    public final String getFile() {
        return file;
    }

    //--------------------------------------------------------------------------

    /**
     * Getter for line property.
     * @return the line number.
     */
    public final int getLine() {
        return line;
    }

    //--------------------------------------------------------------------------

    /**
     * Getter for message property.
     * @return the error message.
     */
    public final String getMessage() {
        return message;
    }
}
