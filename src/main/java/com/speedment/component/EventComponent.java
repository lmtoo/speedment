/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.component;

import com.speedment.annotation.Api;
import com.speedment.event.DefaultEvent;
import com.speedment.event.Event;
import com.speedment.event.UIEvent;
import java.util.function.Consumer;

/**
 * An event bus that is used to let Speedment plugins know when different stages of
 * execution has been reached.
 * 
 * @author Emil Forslund
 * @since  2.3
 * @see    Event
 * @see    DefaultEvent
 */
@Api(version="2.3")
public interface EventComponent extends Component {

    @Override
    public default Class<? extends Component> getComponentClass() {
        return EventComponent.class;
    }
    
    /**
     * Notifies all listeners for this type of event.
     * 
     * @param <E>    the event type
     * @param event  the event that happened
     */
    <E extends Event> void notify(E event);
    
    /**
     * Listens to a particular type of event. The specified action
     * will be called when the appropriate {@link #notify(Event) notify()}
     * method is called.
     * 
     * @param <E>    the event implementation
     * @param event  the event type to listen for
     * @param action the action to call
     */
    <E extends Event> void on(Class<E> event, Consumer<E> action);
    
    /**
     * Listens to a particular type of event. The specified action
     * will be called when the appropriate {@link #notify(Event) notify()}
     * method is called.
     * 
     * @param event  the event type to listen for
     * @param action the action to call
     */
    void on(DefaultEvent event, Consumer<DefaultEvent> action);
    
    /**
     * Listens to a particular type of event. The specified action
     * will be called when the appropriate {@link #notify(Event) notify()}
     * method is called.
     * 
     * @param event  the event type to listen for
     * @param action the action to call
     */
    void on(UIEvent event, Consumer<UIEvent> action);
    
    /**
     * Listens to all kind of events.
     * 
     * @param action  the action to perform on the event
     */
    void onAny(Consumer<Event> action);
}