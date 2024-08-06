"""

Reference: Leetcode 498. Diagonal Traverse

    #    0  1  2
    # 0 [1, 2, 3]
    # 1 [4, 5, 6]
    # 2 [7, 8, 9]

    # 1 (0, 0)
    # 2 (0, 1) -> 4 (1, 0)
    # 3 (0, 2) -> 5 (1, 1) -> 7 (2, 0)
    # 6 (1, 2) -> 8 (2, 1)
    # 9 (2, 2)
"""
from typing import List


class Solution:
    def find_diagonal_order(self, mat: List[List[int]]):
        max_row = len(mat)
        max_col = len(mat[0])
        row = 0
        col = 0

        while col < max_row:
            self.print_each_diagonal(row, col, max_row, max_col, mat)
            if col < max_row:
                col = col + 1

        row = row + 1
        col = max_col - 1

        while row < max_row:
            self.print_each_diagonal(row, col, max_row, max_col, mat)
            if row < max_row:
                row = row + 1

    @staticmethod
    def print_each_diagonal(row: int, col: int, max_row: int, max_col: int, mat: List[List[int]]):
        while 0 <= row < max_row and 0 <= col < max_col:
            print(mat[row][col])
            row = row + 1
            col = col - 1


if __name__ == "__main__":
    s = Solution()
    s.find_diagonal_order([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
