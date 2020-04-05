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
package io.github.sbobrov85.nb.twiglinter.classes;

import java.util.MissingResourceException;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 * Set of common static methods.
 */
public final class CommonHelper {
    /**
     * Contain key for executable path option.
     */
    public static final String OPTION_EXECUTABLE_PATH = "EXECUTABLE_PATH";

    //--------------------------------------------------------------------------

    /**
     * Hide default constructor.
     */
    private CommonHelper() {
    }

    //--------------------------------------------------------------------------

    /**
     * Return localized string.
     * @param key bundle key.
     * @return localized string or key if not exists.
     */
    public static String getLocalizedMessage(final String key) {
        String localizedMessage = null;

        try {
            NbBundle.getMessage(TwigLinter.class, key);
        } catch (MissingResourceException ex) {
            localizedMessage = key;
        }

        return localizedMessage;
    }

    //--------------------------------------------------------------------------

    /**
     * Detect twig file.
     * @param file file for check.
     * @return true, if twig file, false otherwise.
     */
    public static boolean isTwigFile(final FileObject file) {
        return file != null && !file.isFolder()
            && file.getExt().equalsIgnoreCase("twig");
    }
}
