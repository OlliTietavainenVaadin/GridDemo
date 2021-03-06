package org.vaadin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.vaadin.MyAppWidgetset")
public class MyUI extends UI {

    public class Bean {

        public Bean(String firstName, String lastName, Date dateOfBirth) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
        }

        String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        String lastName;
        Date dateOfBirth;
    }

    public List<Bean> getSomeBeans() {
        List<Bean> beanz = new ArrayList<Bean>();
        for (int i = 0; i < 10; i++) {
            Bean bean = new Bean("first" + i, "last" + i, new Date());
            beanz.add(bean);
        }
        beanz.add(getEmptyBean());
        return beanz;
    }

    public Bean getEmptyBean() {
    	return new Bean("", "", null);
    }
    
    public class DateEditorField extends DateField {

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        final BeanItemContainer<Bean> bic = new BeanItemContainer<Bean>(Bean.class,
                getSomeBeans());
        Grid grid = new Grid();
        grid.setContainerDataSource(bic);
        grid.setSizeFull();
        grid.setEditorEnabled(true);

        Column dobColumn = grid.getColumn("dateOfBirth");
        dobColumn.setRenderer(
                new DateRenderer(new SimpleDateFormat("yyyy-MMM-dd")));
        DateField df = new DateField();
        df.setDateFormat("yyyy-MMM-dd");
        grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				// NOOP
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				// TODO Auto-generated method stub
				bic.addBean(getEmptyBean());
			}
        	
        });
        dobColumn.setEditorField(df);

        layout.addComponent(grid);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
