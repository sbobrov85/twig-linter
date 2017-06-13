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
