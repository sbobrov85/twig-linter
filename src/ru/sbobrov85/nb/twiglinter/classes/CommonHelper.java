package ru.sbobrov85.nb.twiglinter.classes;

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
            //todo write log warning
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
