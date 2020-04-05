package io.github.sbobrov85.nb.twiglinter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.windows.OnShowing;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import io.github.sbobrov85.nb.twiglinter.classes.CommonHelper;
import io.github.sbobrov85.nb.twiglinter.classes.TwigLinterErrorAnnotation;

/**
 * Add listeners on open and save file.
 */
@OnShowing
public class TwigLinterInstaller implements Runnable {
    /**
     * {@inheritDoc}
     */
    @Override
    public final void run() {
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(
            new TwigLinterInstallerListener()
        );
    }

    //--------------------------------------------------------------------------

    /**
     * Property change listener subclass.
     */
    class TwigLinterInstallerListener implements PropertyChangeListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("opened")) {
                HashSet<TopComponent> newHashSet;
                newHashSet = (HashSet<TopComponent>) evt.getNewValue();
                for (TopComponent topComponent : newHashSet) {
                    DataObject dObj = topComponent.getLookup()
                        .lookup(DataObject.class);
                    if (dObj != null) {
                        FileObject currentFile = dObj.getPrimaryFile();
                        if (currentFile != null
                            && CommonHelper.isTwigFile(currentFile)
                        ) {
                            TwigLinterErrorAnnotation
                                .createAnnotations(currentFile);
                            currentFile.addFileChangeListener(
                                new FileChangeAdapter() {
                                    /**
                                     * {@inheritDoc}
                                     */
                                    @Override
                                    public void fileChanged(final FileEvent fe) {
                                        TwigLinterErrorAnnotation
                                            .createAnnotations(fe.getFile());
                                    }
                                }
                            );
                        }
                    }
                }
            }
        }
    }
}
