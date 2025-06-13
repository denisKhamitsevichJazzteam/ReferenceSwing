package org.jazzteam.task;

import org.jazzteam.task.listener.TaskListener;

/**
 * Represents a UI component that can be refreshed or redrawn.
 *
 * <p>This interface is typically implemented by Swing components (e.g., dialogs, panels, tables)
 * that need to update their contents in response to the completion of a background task.
 *
 * <p>The {@code update()} method is called from the Event Dispatch Thread (EDT)
 * after a task finishes via a {@link TaskListener}.
 */
public interface Updatable {
    /**
     * Refreshes or updates the component’s state or UI.
     * Called after a task has completed successfully.
     */
    void update();
}
