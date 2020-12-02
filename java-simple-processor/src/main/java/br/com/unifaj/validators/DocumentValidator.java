package br.com.unifaj.validators;

import br.com.unifaj.interfaces.Validator;

public class DocumentValidator implements Validator {

    private String getDigit(String value) {
        int digitSum = 0;
        int documentLength = value.length();

        for(int index = documentLength; index > 0; index--){
            int treatIndex = documentLength - (index - 1);
            digitSum += Integer.parseInt(value.substring(treatIndex - 1, treatIndex)) * (index + 1);
        }

        int module = digitSum%11;
        int digit = (11 - module) >= 10 ? 0 : (11 - module);

        return Integer.toString(digit);
    }

    private Boolean checkIfIsNumeric(String value) {

        try {
            Long.parseLong(value);
        }catch (Exception ex){
            return false;
        }

        return true;
    }

    private Boolean checkLength(String value) {
        return value.length() == 11;
    }

    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();

        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    @Override
    public Boolean validate(String value) {

        try {
            value = padLeftZeros(value, 11);

            if(checkIfIsNumeric(value) && checkLength(value)) {
                String validated = value.substring(0, value.length() - 2);
                validated += this.getDigit(validated);
                validated += this.getDigit(validated);

                return validated.equals(value);
            }
        }catch (Exception ex) {
            System.out.println("Error on document validation.\nDetails:" + ex.getMessage());
        }

        return false;
    }

}
