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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * Class for lint twig files.
 */
public final class TwigLinter {
    /**
     * Hide default constructor.
     */
    private TwigLinter() {
    }

    //--------------------------------------------------------------------------

    /**
     * Lint twig file.
     * @param fo FileObject linked with twig file.
     * @return LinkedList<TwigError> errors list.
     */
    public static LinkedList<TwigError> lint(final FileObject fo) {
        LinkedList<TwigError> result = new LinkedList<>();

        try {
            File file = FileUtil.toFile(fo);
            BufferedReader linterResult = executeLinter(file.getAbsolutePath());
            String outPart;
            while ((outPart = linterResult.readLine()) != null) {
                TwigError error = new TwigError(outPart);
                if (!"".equals(error.getMessage())) {
                    result.push(error);
                }
            }
        } catch (IOException | InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return result;
    }

    //--------------------------------------------------------------------------

    /**
     * Executing twig linter for file and get output.
     * @param fileName full twig file name.
     * @return BufferReader linter console output.
     * @throws IOException on executing error.
     * @throws InterruptedException on interrupt process.
     */
    protected static BufferedReader executeLinter(final String fileName)
        throws IOException, InterruptedException {
        Preferences prefs = NbPreferences.forModule(TwigLinter.class);
        String linter = prefs.get(CommonHelper.OPTION_EXECUTABLE_PATH, "");

        String phpInterpreter = NbPreferences.root()
            .node("org/netbeans/modules/php/project/general")
            .get("phpInterpreter", "php");

        Runtime runtime = Runtime.getRuntime();
        String[] commands = {
            phpInterpreter,
            linter,
            "lint",
            "--format=csv",
            fileName
        };

        Process process = runtime.exec(commands);
        process.waitFor();

        return new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );
    }
}
