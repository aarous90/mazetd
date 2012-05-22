/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: EntityEvent.java
 * Type: events.EntityEvent
 * 
 * Documentation created: 22.05.2012 - 21:46:30 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package events;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;
import entities.base.AbstractEntity;

/**
 * The class EntityEvent for all events of an entity.
 * @author Hans Ferchland
 * @version
 */
public class EntityEvent extends AbstractEvent {

    public enum EntityEventType {

        Click,
        MouseOver,
        MouseLeft
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private AbstractEntity entity;
    private EntityEventType eventType;
    private Vector2f mouse;
    private CollisionResult result;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public EntityEvent(Object source, AbstractEntity entity, EntityEventType eventType) {
        super(source);
        this.entity = entity;
        this.eventType = eventType;
    }

    public EntityEvent(
            Object source,
            AbstractEntity entity,
            EntityEventType eventType,
            Vector2f mouse,
            CollisionResult result) {
        super(source);
        this.entity = entity;
        this.eventType = eventType;
        this.mouse = mouse;
        this.result = result;
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public EntityEventType getEventType() {
        return eventType;
    }

    public Vector2f getMouse() {
        return mouse;
    }

    public CollisionResult getResult() {
        return result;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
