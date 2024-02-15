from config.color import Color
from config.config_class import OrnamentApplicationConfig

from runner import OrnamentAppRunner

if __name__ == "__main__":
    config = OrnamentApplicationConfig(
        images_amount_filepath="/Users/snezanavaleeva/PycharmProjects/OrnamentDrawerPygame/resources/images_amount.txt",
        ornaments_save_dir="/Users/snezanavaleeva/Desktop/ornaments/",
        repeats=8,
        rect_width=135,
        ornament_colors=[
            Color("Light Beige", 0xB3A99E),
            Color("Dark Beige", 0x876E50),
            Color("Light Brown", 0x553A21),
            Color("Dark Brown", 0x241C1A),
            Color("Green", 0x2F3120)
        ]
    )

    runner = OrnamentAppRunner(config)
    runner.run()
