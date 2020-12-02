from random import sample
import numpy as np
import json

class Util:

    def __init__(self):
        self.__INITIALIZED = True

    def __get_digit(self, cpf):
        digit_sum = 0

        for i in range(len(cpf), 0, -1):
            index = len(cpf) - i 

            digit_sum += int(cpf[index]) * (i + 1)

        module = digit_sum%11
        digit = 0 if ((11 - module) >= 10) else (11 - module) 
    
        return str(digit)

    def generate_valid_cpf(self):
        document = ''.join(map(str, sample(range(0, 10), 9)))
        document += self.__get_digit(document)
        document += self.__get_digit(document)

        return document

    def generate_invalid_cpf(self):
        document = ''.join(map(str, np.random.uniform(low=0, high=10, size=(11,)).astype(int)))
       
        if( not self.validate_cpf(document)):
            return document
        else:
            return self.generate_invalid_cpf()

    def validate_cpf(self, cpf):
        
        trusted = cpf[0:9]
        trusted += self.__get_digit(trusted)
        trusted += self.__get_digit(trusted)

        return cpf == trusted

    def load_base(self, attribute):
        with open('../../../resources/db/base.json') as json_file:
            json_data = json.load(json_file)
            return [item for item in json_data if item['group'] == attribute][0]['values']