import pygame

from config.config_class import OrnamentApplicationConfig
from utilities.file_utility import save_images_amount, get_saved_image_number
from drawer.draw_and_analise import OrnamentAnalysingDrawer


class OrnamentAppRunner:
    def __init__(self, config: OrnamentApplicationConfig):
        if not isinstance(config, OrnamentApplicationConfig):
            raise ValueError("incorrect type of config" + str(type(config)))
        self.config = config

    def run(self):
        image_number = get_saved_image_number(self.config.images_amount_filepath)
        pygame.init()
        screen = pygame.display.set_mode((self.config.get_image_width(), self.config.get_image_height()))
        ornament_drawer = OrnamentAnalysingDrawer(self.config.ornament_colors,
                                                  screen,
                                                  self.config.rect_width,
                                                  self.config.get_rect_height(),
                                                  self.config.repeats)
        ornament_drawer.draw_new_ornament()
        running = True
        while running:
            for event in pygame.event.get():
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_s:
                        ornament_drawer.save_ornament(self.config.ornaments_save_dir, image_number)
                        image_number += 1
                    if event.key == pygame.K_SPACE:
                        ornament_drawer.draw_new_ornament()
                if event.type == pygame.QUIT:
                    save_images_amount(self.config.images_amount_filepath, image_number)
                    running = False
                    break

        pygame.quit()
