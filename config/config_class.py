from dataclasses import dataclass
import math

from config.color import Color


@dataclass()
class OrnamentApplicationConfig:
    ornaments_save_dir: str
    repeats: int
    rect_width: int
    ornament_colors: list[Color]
    images_amount_filepath: str = ""

    def get_image_width(self):
        return int(self.rect_width / math.sqrt(2)) * self.repeats

    def get_image_height(self):
        return int(self.rect_width / math.sqrt(2)) * self.repeats

    def get_rect_height(self):
        return int(self.rect_width / math.sqrt(2))
