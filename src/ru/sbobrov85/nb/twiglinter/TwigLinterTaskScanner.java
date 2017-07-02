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
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import ru.sbobrov85.nb.twiglinter.classes.CommonHelper;
import ru.sbobrov85.nb.twiglinter.classes.TwigLinterErrorAnnotation;

/**
 * Main class for file scan control.
 * Scaning twig files, create annotations and tasks.
 */
public class TwigLinterTaskScanner extends FileTaskScanner {

    /**
     * Contains group name constant.
     */
    private static final String GROUP_NAME = "nb-tasklist-error";

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

        List<TwigLinterErrorAnnotation> annotations = null;
        try {
            annotations = TwigLinterErrorAnnotation
                .getAnnotationList(DataObject.find(fo));
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (annotations != null) {
            for (TwigLinterErrorAnnotation annotation: annotations) {
                boolean isTwigAnnotaion = "twiglinter-annotation"
                    .equals(annotation.getAnnotationType());
                if (isTwigAnnotaion) {
                    tasks.add(Task.create(
                        fo,
                        GROUP_NAME,
                        annotation.getShortDescription(),
                        annotation.getLineNumber() + 1
                    ));
                }
            }
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
