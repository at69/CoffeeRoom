package com.coffeeroom.validators;

import com.coffeeroom.util.JsfUtil;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="confirmValidator")
public class ConfirmationPasswordValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIInput passwordComponent = (UIInput) component.getAttributes().get("password");
        String password = (String) passwordComponent.getValue();
        String confirmPassword = (String) value;
        if (confirmPassword != null && !confirmPassword.equals(password)) {
            JsfUtil.addErrorMessage(new FacesException(), "Confirm password is not the same as password");
            throw new ValidatorException(new ArrayList<FacesMessage>());
        }
    }

}
