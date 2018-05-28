package football.analyze.main.common;

import lombok.Getter;

/**
 * @author Hassan Mushtaq
 * @since 5/25/18
 */
@Getter
public abstract class Entity {

    private String id;

    protected Entity() {
    }

    protected Entity(String id) {
        this.id = id;
    }
}
