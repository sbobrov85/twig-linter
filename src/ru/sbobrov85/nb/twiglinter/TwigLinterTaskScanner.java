package ru.sbobrov85.nb.twiglinter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import ru.sbobrov85.nb.twiglinter.classes.TwigLinter;
import ru.sbobrov85.nb.twiglinter.classes.CommonHelper;
import ru.sbobrov85.nb.twiglinter.classes.TwigError;
import ru.sbobrov85.nb.twiglinter.classes.TwigLinterErrorAnnotation;

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

        DataObject dataObject = null;
        LineCookie lineCookie = null;

        try {
            dataObject = DataObject.find(fo);
            lineCookie = dataObject.getLookup().lookup(LineCookie.class);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (dataObject != null) {
            TwigLinterErrorAnnotation.clear(dataObject);
        }

        final LinkedList<Task> tasks = new LinkedList<>();

        LinkedList<TwigError> errors = TwigLinter.lint(fo);

        for (TwigError error : errors) {
            if (dataObject != null && lineCookie != null) {
                TwigLinterErrorAnnotation.createAnnotation(
                    dataObject,
                    lineCookie,
                    error
                );
            }
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
