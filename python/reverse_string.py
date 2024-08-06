def reverse_string_with_two_pointers(input_string: str) -> str:
    """
    Function to reverse a string.
    :param input_string: string to be reversed.
    :return: reversed string.
    """
    # Convert the string to a list
    input_list = list(input_string)
    # Initialize left pointer at the start of the list
    left = 0
    # Initialize right pointer at the end of the list
    right = len(input_list) - 1
    # Loop until the left pointer is less than the right pointer
    while left < right:
        # Swap the elements at the left and right pointers
        temp = input_list[left]
        input_list[left] = input_list[right]
        input_list[right] = temp
        # Move the right pointer one step towards the start
        right -= 1
        # Move the left pointer one step towards the end
        left += 1

    # Join the list back into a string and return
    return ''.join(s for s in input_list)


# Driver code
def reverse_string(input_string: str) -> str:
    reversed_string = ''
    for c in input_string:
        reversed_string = c + reversed_string

    return reversed_string


def transpose_string(input_string: str) -> str:
    split_words = input_string.split(' ')
    reversed_words = ''
    for word in split_words:
        reversed_words = word + ' ' + reversed_words

    return ''.join(reversed_words)


if __name__ == '__main__':
    # Test the function with a sample string
    print(reverse_string_with_two_pointers('Hello World! with two pointers'))
    print(reverse_string('Hello World! with one pointer'))
    print(transpose_string('Hello World! with one pointer'))
