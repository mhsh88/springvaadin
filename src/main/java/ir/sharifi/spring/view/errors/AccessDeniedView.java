package ir.sharifi.spring.view.errors;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import ir.sharifi.spring.view.exceptions.AccessDeniedException;

import javax.servlet.http.HttpServletResponse;

@Tag("access-denied-view")
@HtmlImport("src/views/errors/access-denied-view.html")
@PageTitle("Access denied")
@Route
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
