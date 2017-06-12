package ru.sbobrov85.nb.twiglinter.classes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.openide.cookies.LineCookie;
import org.openide.loaders.DataObject;
import org.openide.text.Annotatable;
import org.openide.text.Annotation;
import org.openide.text.Line;

/**
 * Class for twig error annotations managing.
 */
public final class TwigLinterErrorAnnotation extends Annotation {

    /**
     * Contains annotations list.
     */
    private static final
        Map<DataObject, List<Annotation>> annotations = new HashMap<>();;

    /**
     * Contains annotation message.
     */
    private static String message;

    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * @param msg message for annotation.
     */
    private TwigLinterErrorAnnotation(final String msg) {
        message = msg;
    }

    //--------------------------------------------------------------------------

    /**
     * Get annotations list.
     * @param dataObject current dataObject.
     * @return List<Annotation> annotations list.
     */
    public static List<Annotation> getAnnotationList(
        final DataObject dataObject
    ) {
        if (null == annotations.get(dataObject)) {
            annotations.put(dataObject, new ArrayList<Annotation>());
        }
        return annotations.get(dataObject);
    }

    //--------------------------------------------------------------------------

    /**
     * Clear annotations list.
     * @param dataObject current dataObject.
     */
    public static void clear(final DataObject dataObject) {
        for (Annotation annotation : getAnnotationList(dataObject)) {
            annotation.detach();
        }
        getAnnotationList(dataObject).clear();
    }

    //--------------------------------------------------------------------------

    /**
     * Remove annotations list.
     * @param dataObject current dataObject.
     * @param annotation annotation instance.
     */
    public static void remove(
        final DataObject dataObject,
        final TwigLinterErrorAnnotation annotation
    ) {
        getAnnotationList(dataObject).remove(annotation);
    }

    //--------------------------------------------------------------------------

    /**
     * Get annotation type.
     * @return annotation type name.
     */
    @Override
    public String getAnnotationType() {
        return "twiglinter-annotation";
    }

    //--------------------------------------------------------------------------

    /**
     * Get short description for annotation.
     * @return message as short description.
     */
    @Override
    public String getShortDescription() {
        return message;
    }

    //--------------------------------------------------------------------------

    /**
     * Create twig error annotation.
     * @param dataObject current data object.
     * @param lineCookie line cookie.
     * @param error twig error object.
     */
    public static void createAnnotation(
        final DataObject dataObject,
        final LineCookie lineCookie,
        final TwigError error
    ) {
        final Line currentLine = lineCookie.getLineSet()
            .getCurrent(error.getLine() - 1);
        final Line.Part currentPartLine = currentLine.createPart(0, 1);

        final TwigLinterErrorAnnotation annotation = new TwigLinterErrorAnnotation(error.getMessage());
        getAnnotationList(dataObject).add(annotation);

        annotation.attach(currentPartLine);

        currentPartLine.addPropertyChangeListener(new PropertyChangeListener() {

            /**
             * Manage annotations on change event.
             * @param ev current event.
             */
            @Override
            public void propertyChange(final PropertyChangeEvent ev) {
                String type = ev.getPropertyName();
                if (type == null || type.equals(Annotatable.PROP_TEXT)) {
                    currentPartLine.removePropertyChangeListener(this);
                    annotation.detach();
                    TwigLinterErrorAnnotation.remove(dataObject, annotation);
                }
            }
        });
    }
}
