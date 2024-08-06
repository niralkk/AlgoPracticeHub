"""2028. Find Missing Observations

You have observations of n + m 6-sided dice rolls with each face numbered from 1
to 6. n of the observations went missing, and you only have the observations of m rolls. Fortunately, you have also
calculated the average value of the n + m rolls.

You are given an integer array rolls of length m where rolls[i] is the value of the ith observation. You are also
given the two integers mean and n.

Return an array of length n containing the missing observations such that the average value of the n + m rolls is
exactly mean. If there are multiple valid answers, return any of them. If no such array exists, return an empty array.

The average value of a set of k numbers is the sum of the numbers divided by k.

Note that mean is an integer, so the sum of the n + m rolls should be divisible by n + m.

Input: rolls = [3,2,4,3], mean = 4, n = 2
Output: [6,6]
Explanation: The mean of all n + m rolls is (3 + 2 + 4 + 3 + 6 + 6) / 6 = 4.

"""
from typing import List


class Solution:
    @staticmethod
    def missingRolls(rolls: List[int], mean: int, n: int) -> List[int]:
        m = len(rolls)
        m_total = sum(rolls)
        n_total = (mean * (m + n)) - m_total

        output = []
        if n_total < n or n_total > 6 * n:
            return output

        '''
        n_total = 14, n = 10
        n_total = 6, new n_total = 8, n = 9 
        '''

        while n_total > 0:
            dice = min(6, n_total - n + 1)
            output.append(dice)
            n_total = n_total - dice
            n = n - 1

        return output


if __name__ == '__main__':
    s = Solution
    print(s.missingRolls([3, 2, 4, 3], 4, 2))
