from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def __init__(self):
        self.max_diameter = None

    def diameter_of_binary_tree(self, root: Optional[TreeNode]) -> int:
        self.max_diameter = 0
        self.height(root)
        return self.max_diameter

    def height(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0

        left_height = self.height(root.left)
        right_height = self.height(root.right)
        diameter = left_height + right_height

        self.max_diameter = max(self.max_diameter, diameter)

        return max(left_height, right_height) + 1
