import random


def check_eq_pairs(level_pairs: list[list[int]]):
    for pair1_index in range(len(level_pairs) - 1):
        for pair2_index in range(pair1_index + 1, len(level_pairs)):
            if level_pairs[pair1_index][0] == level_pairs[pair2_index][0]:
                if level_pairs[pair1_index][1] == level_pairs[pair2_index][1]:
                    return True
    return False


class MatrixRandomFiller:

    def __init__(self, repeats: int, values_amount: int):
        if (repeats <= 0) or (values_amount <= 0):
            raise ValueError("repeats and values amount must be positive")
        self.repeats = repeats
        self.entry = [[0 for _ in range(repeats)] for _ in range(repeats - 1)]
        self.values_amount = values_amount
        self.refill()

    def randomize(self):
        numbers = list(range(self.values_amount))
        for i in range(self.repeats - 1):
            for j in range(self.repeats):
                random_number = random.choice(numbers)

                while ((i > 0 and self.entry[i - 1][j] == random_number) or
                       (j > 0 and self.entry[i][j - 1] == random_number)):
                    random_number = random.choice(numbers)

                self.entry[i][j] = random_number

    def check_horizontal_pairs(self):
        for row_index in range(1, len(self.entry)):
            level_pairs = []
            for j in range(0, len(self.entry[0])):
                level_pairs.append([self.entry[row_index - 1][j], self.entry[row_index][j]])
            if check_eq_pairs(level_pairs):
                return True
        return False

    def check_vertical_pairs(self):
        col_index = 0

        while col_index < len(self.entry[0]):
            level_pairs = []
            for i in range(1, len(self.entry), 2):
                level_pairs.append([self.entry[i - 1][col_index], self.entry[i][col_index]])
            if check_eq_pairs(level_pairs):
                return True
            col_index += 1
        return False

    def check_many_repeats(self):
        if self.values_amount < 5:
            return False
        return self.check_horizontal_pairs() and self.check_vertical_pairs()

    def refill(self):
        self.randomize()
        many_value_repeats = self.check_many_repeats()
        while many_value_repeats:
            self.randomize()
            many_value_repeats = self.check_many_repeats()
