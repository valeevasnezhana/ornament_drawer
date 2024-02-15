def get_saved_image_number(file_path):
    try:
        with open(file_path) as file:
            return int(file.readline())
    except FileNotFoundError:
        return 0
    except ValueError:
        return 0


def save_images_amount(file_path: str, image_number: int):
    with open(file_path, 'w') as file:
        file.write(f'{image_number}')
