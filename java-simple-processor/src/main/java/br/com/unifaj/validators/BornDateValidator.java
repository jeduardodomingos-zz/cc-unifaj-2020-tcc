package br.com.unifaj.validators;

import br.com.unifaj.interfaces.Validator;

import java.text.SimpleDateFormat;

public class BornDateValidator  implements Validator {

    @Override
    public Boolean validate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            sdf.parse(value);
        }catch (Exception ex) {
            return false;
        }

        return true;
    }

}
