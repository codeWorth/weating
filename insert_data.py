import json
import sys


def insert_data(input_file_name: str, info_file_name: str):
    output_file_name = input_file_name.replace(".db_template", "")
    print(f"Writing data from {input_file_name} to {output_file_name} using {info_file_name}")
    with open(input_file_name, "r") as input_file, \
            open(output_file_name, "w") as output_file, \
            open(info_file_name, "r") as info_file:
        data: str = input_file.read()
        info: dict = json.loads(info_file.read())

        info_name = info_file_name.split("/")[-1].split(".")[0]
        for key, value in info.items():
            data = data.replace(f"{{{info_name}.{key}}}", value)

        output_file.write(data)


if __name__ == '__main__':
    insert_data("jooq-codegen.xml.db_template", sys.argv[1])
    insert_data("src/main/resources/application.properties.db_template", sys.argv[1])
    insert_data(".run/Create Tables.run.xml.db_template", sys.argv[1])
