package ch.bbw.pg.olat.views.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;

public interface FormView {

    default void save(ClickEvent<Button> clickEvent) {
    }

    default void cancel(ClickEvent<Button> clickEvent) {
    }
}
