from utils.util import Util
import random
import sys
import logging
from datetime import datetime

__UTIL = Util()
__LOGGER = logging.getLogger("DataGeneratorLogger")

__NAMES = __UTIL.load_base("names")
__ADDRESS = __UTIL.load_base("address")
__CITIES = __UTIL.load_base("city")


def generate(cpf_lines):
    print(f"Starting Data Generator Application for {cpf_lines} records at {datetime.now()}")

    f = open("../../../output/generated.csv", "a+")

    header = "id;name;cpf;address;city;state;parent_name;parent_document;parent_city;parent_state;"
    f.write(header)

    for index in range(cpf_lines):

        if(index%2 == 0):
            cpf = __UTIL.generate_valid_cpf()
            parent_cpf = __UTIL.generate_valid_cpf()
        else:
            cpf = __UTIL.generate_invalid_cpf()
            parent_cpf = __UTIL.generate_invalid_cpf()
        
        city = random.choice(__CITIES)
        parent_city = random.choice(__CITIES)

        f.write(f"{index};{random.choice(__NAMES)};{cpf};{city['name']};{city['state']};{random.choice(__NAMES)};{parent_cpf};{parent_city['name']};{parent_city['state']};\n")

    f.close()

    print(f"Finishing Data Generator Application for {cpf_lines} records at {datetime.now()}")


if __name__ == "__main__":

    arguments = sys.argv[1:]
    cpf_lines = int(arguments[0])
    
    generate(cpf_lines)