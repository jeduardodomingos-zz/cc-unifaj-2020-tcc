package br.com.unifaj.validators;

import br.com.unifaj.interfaces.Validator;

public class DriverLicenseValidator implements Validator {

    @Override
    public Boolean validate(String value) {

        if(checkIfIsNumeric(value) && checkLength(value)) {
            return checkDigit(value);
        }

        return false;
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

    private boolean checkDigit(String cnhNumber) {
        Long sum = 0L;
        Long firstModuleResult = 0L;
        Long secondModuleResult = 0L;
        Integer currentDigit;
        Long validatedDigit;

        Long firstDigit = Long.parseLong(cnhNumber.substring(9,10));
        Long secondDigit = Long.parseLong(cnhNumber.substring(10,11));

        boolean firstDigitValidated = false;
        boolean secondDigitValidated = false;

        for(int index = 9; index >= 1; index--){
            currentDigit = Integer.parseInt(cnhNumber.substring((9 - index), ((9 - index) + 1)));
            sum += (currentDigit * index);
        }

        firstModuleResult = sum%11;

        validatedDigit = firstModuleResult > 9 ? 0 : firstModuleResult;

        if(validatedDigit.equals(firstDigit)){
            firstDigitValidated = true;
        }

        sum = 0L;

        for (int index = 1; index <= 9; index++){
            currentDigit = Integer.parseInt(cnhNumber.substring((index - 1), ((index - 1) + 1)));
            sum += (currentDigit * index);
        }

        secondModuleResult = sum%11;

        if(firstModuleResult > 9){
            if((secondModuleResult - 2) < 0){
                secondModuleResult += 9;
            }else {
                secondModuleResult -= 2;
            }
        }

        validatedDigit = secondModuleResult > 9 ? 0 : secondModuleResult;

        if(validatedDigit.equals(secondDigit)){
            secondDigitValidated = true;
        }

        return firstDigitValidated && secondDigitValidated;
    }


}
