/*
 * Copyright (C) 2017 sbobrov85 <sbobrov85@gmail.com>.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
        List<String> parts = Arrays.asList(csvLine.split("\\s*,\\b"));
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
