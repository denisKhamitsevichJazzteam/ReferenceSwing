package org.jazzteam.core;

import lombok.Getter;
import org.jazzteam.event.EventBroker;
import org.jazzteam.event.EventDispatcher;
import org.jazzteam.repository.PriorityDAO;
import org.jazzteam.repository.TodoDAO;
import org.jazzteam.service.PriorityService;
import org.jazzteam.service.TodoService;

/**
 * ApplicationContext serves as a centralized registry for application-wide
 * services and repositories, similar to the concept of an ApplicationContext in
 * the Spring Framework.
 *
 * <p>This class provides static access to singleton instances of repositories
 * and services used throughout the application, promoting a simple form of
 * dependency management and inversion of control (IoC).
 *
 * <p><b>Note:</b> Spring Framework was not used in this implementation, as
 * the task did not include it in the technology stack. This is a simplified
 * manual approach to managing dependencies.
 */
public class ApplicationContext {
    @Getter
    private static final TodoDAO todoDAO = new TodoDAO();
    @Getter
    private static final PriorityDAO priorityDAO = new PriorityDAO();

    @Getter
    private static final TodoService todoService = new TodoService(todoDAO);
    @Getter
    private static final PriorityService priorityService = new PriorityService(priorityDAO);

    @Getter
    private static final EventDispatcher eventDispatcher = new EventDispatcher();
    @Getter
    private static final EventBroker eventBroker = new EventBroker(eventDispatcher);

}
