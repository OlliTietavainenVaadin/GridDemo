package com.vaadin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
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

    public class Location {
        String name;
        String hiddenInfo;

        public String getName() {
            return name;
        }

        public void setName(String label) {
            name = label;
        }

        public String getHiddenInfo() {
            return hiddenInfo;
        }

        public void setHiddenInfo(String hiddenInfo) {
            this.hiddenInfo = hiddenInfo;
        }
    }

    public class Bean {

        public Bean(String firstName, String lastName, Date dateOfBirth) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            location = new Location();
            location.setName("foo");
            location.setHiddenInfo("bar");
        }

        String firstName;

        Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

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
        for (int i = 0; i < 50; i++) {
            Bean bean = new Bean("first" + i, "last" + i, new Date());
            beanz.add(bean);
        }
        return beanz;
    }

    public class DateEditorField extends DateField {

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        BeanItemContainer<Bean> bic = new BeanItemContainer<Bean>(Bean.class,
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
        dobColumn.setEditorField(df);

        Column locationColumn = grid.getColumn("location");
        locationColumn.setConverter(new Converter<String, Location>() {

            @Override
            public Location convertToModel(String value,
                    Class<? extends Location> targetType, Locale locale)
                            throws com.vaadin.data.util.converter.Converter.ConversionException {
                // Not used in this example
                return null;
            }

            @Override
            public String convertToPresentation(Location value,
                    Class<? extends String> targetType, Locale locale)
                            throws com.vaadin.data.util.converter.Converter.ConversionException {
                return value.name;
            }

            @Override
            public Class<Location> getModelType() {
                return Location.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }

        });

        layout.addComponent(grid);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
