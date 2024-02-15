from config.color import Color


class ColorStatistics:
    def __init__(self, color: Color):
        self.stats = None
        self.color = color
        self.clear_stats()

    def add_half(self, bottom: bool, left: bool):
        if bottom:
            self.stats['half_left_bottom' if left else 'half_right_bottom'] += 1
        else:
            self.stats['half_left_up' if left else 'half_right_up'] += 1

    def add_full(self, left: bool):
        self.stats['full_left' if left else 'full_right'] += 1

    def clear_stats(self):
        self.stats = {'full_left': 0,
                      'full_right': 0,
                      'half_left_up': 0,
                      'half_right_up': 0,
                      'half_left_bottom': 0,
                      'half_right_bottom': 0
                      }

    def __str__(self):
        stats = ', '.join(f'{key}={value}' for key, value in self.stats.items())
        return f"{self.color.name}: {stats}"
