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
package ru.sbobrov85.nb.twiglinter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import ru.sbobrov85.nb.twiglinter.classes.TwigLinter;
import ru.sbobrov85.nb.twiglinter.classes.CommonHelper;
import ru.sbobrov85.nb.twiglinter.classes.TwigError;

/**
 * Main class for file scan control.
 * Scaning twig files, create annotations and tasks.
 */
public class TwigLinterTaskScanner extends FileTaskScanner {

    /**
     * Contains group name constant.
     */
    private static final String GROUP_NAME = "twiglinter-tasklist";

    /**
     * Contains callback instance link.
     */
    private Callback callback;

    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * @param label text label for twig scanner.
     * @param description text description for twig scanner.
     */
    public TwigLinterTaskScanner(final String label, final String description) {
        super(label, description, null);
    }

    //--------------------------------------------------------------------------

    /**
     * Create and return twig scanner instance.
     * @return TwigLinterTaskScanner instance.
     */
    public static TwigLinterTaskScanner create() {
        return new TwigLinterTaskScanner(
            CommonHelper.getLocalizedMessage("TaskScanner.Label"),
            CommonHelper.getLocalizedMessage("TaskScanner.Description")
        );
    }

    //--------------------------------------------------------------------------

    /**
     * Execute task for file scan.
     * @param fo file for scan.
     * @return LinkedList<Task> errors tasks for scanned file.
     */
    @Override
    public final List<? extends Task> scan(final FileObject fo) {
        if (!CommonHelper.isTwigFile(fo)) {
            return Collections.<Task>emptyList();
        }

        final LinkedList<Task> tasks = new LinkedList<>();

        LinkedList<TwigError> errors = TwigLinter.lint(fo);

        for (TwigError error : errors) {
            tasks.add(Task.create(
                fo,
                GROUP_NAME,
                error.getMessage(),
                error.getLine()
            ));
        }

        return tasks;
    }

    //--------------------------------------------------------------------------

    /**
     * Attach current callback to object property.
     * @param clbck current callback.
     */
    @Override
    public final void attach(final Callback clbck) {
        callback = clbck;
    }
}
