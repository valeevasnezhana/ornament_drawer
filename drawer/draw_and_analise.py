import os
from typing import Optional

import pygame
from dataclasses import dataclass, field

from config.color import Color
from analise.color_statistics import ColorStatistics
from utilities.matrix_filler import MatrixRandomFiller
from analise.ornament_analyser import analise_pieces_amount


@dataclass()
class OrnamentAnalysingDrawer:
    colors: list[Color]
    screen: pygame.Surface
    rect_width: int
    rect_height: int
    repeats: int = 8
    color_choice_matrix: Optional[MatrixRandomFiller] = field(default=None, init=False)
    color_stats: Optional[list[ColorStatistics]] = field(default=None, init=False)

    def __post_init__(self):
        if self.color_stats is None:
            self.color_stats = [ColorStatistics(color) for color in self.colors]
        if self.color_choice_matrix is None:
            self.color_choice_matrix = MatrixRandomFiller(self.repeats, len(self.colors))

    def draw_new_ornament(self):
        self.color_choice_matrix.refill()

        self.draw_even_rects()
        self.draw_odd_rects()

        self.screen.blit(pygame.transform.rotate(self.screen, 90), (0, 0))

        pygame.display.flip()

    def save_ornament(self, save_dir: str, image_number: int):
        image_path = os.path.join(save_dir, f"orn_{image_number}.JPG")
        pygame.image.save(self.screen, image_path)

        stats_path = os.path.join(save_dir, f"orn_{image_number}_stats.txt")
        with open(stats_path, "w") as file:
            analise_pieces_amount(self.color_choice_matrix.entry, self.color_stats)
            for stat in self.color_stats:
                file.write(f"{stat}\n")

    def draw_even_rects(self):
        x = -self.rect_height // 2 + 6
        y = 0
        for j in range(0, self.repeats, 2):
            for i in range(self.repeats - 1):
                color = self.colors[self.color_choice_matrix.entry[i][j]]
                dots = [
                    (x, y),
                    (x + self.rect_width, y),
                    (x + self.rect_width - self.rect_height, y + self.rect_height),
                    (x - self.rect_height, y + self.rect_height)
                ]
                pygame.draw.polygon(self.screen, color.value, dots, width=0)

                x += self.rect_width
            x = -self.rect_height // 2 + 6
            y += 2 * self.rect_height

    def draw_odd_rects(self):
        x = -3
        y = self.rect_height

        for j in range(1, self.repeats, 2):
            for i in range(self.repeats - 1):
                color = self.colors[self.color_choice_matrix.entry[i][j]]
                dots = [
                    (x, y),
                    (x - self.rect_width, y),
                    (x - self.rect_width + self.rect_height, y + self.rect_height),
                    (x + self.rect_height, y + self.rect_height)
                ]

                pygame.draw.polygon(self.screen, color.value, dots, width=0)
                x += self.rect_width
            x = -3
            y += 2 * self.rect_height
