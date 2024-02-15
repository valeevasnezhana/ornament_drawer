from analise.color_statistics import ColorStatistics


def analise_pieces_amount(color_choice_matrix: list[list[int]], color_stats: list[ColorStatistics]):
    left = True
    for stat in color_stats:
        stat.clear_stats()

    for index, mat in enumerate(color_choice_matrix):
        half = False
        bottom = True
        if index == 0:
            half = True
        if index == len(color_choice_matrix) - 1:
            half = True
            bottom = False
        for x in mat:
            if half:
                color_stats[x].add_half(bottom, left)
            else:
                color_stats[x].add_full(left)
            left = not left
