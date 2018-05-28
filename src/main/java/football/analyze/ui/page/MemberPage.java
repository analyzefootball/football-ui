package football.analyze.ui.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class MemberPage extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void enter(ViewChangeEvent event) {
		Label title = new Label();
		
		title.setCaption("Member");
		title.setValue("Member view");
		
		addComponent(title);
	}
}