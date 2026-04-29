def load_prompt(file_name):
    with open(f"prompts/{file_name}", "r") as file:
        return file.read()