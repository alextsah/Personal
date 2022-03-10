#!/usr/bin/env python3

# ECSE211 - Design Principles and Methods
# Assignment 2 - Introduction to Python

# Assignment setup: DO NOT MODIFY

from __future__ import annotations
from statistics import mean  # mean = average
from textwrap import dedent
from types import SimpleNamespace as ColorNames
COLOR = ColorNames(VIOLET="\033[95m", BLUE="\033[94m", CYAN="\033[96m", GREEN="\033[92m", YELLOW="\033[93m",
                   ORANGE="\u001b[31;1m", RED="\033[91m", ENDC="\033[0m")
color_str = lambda color, text: f"{color}{text}{COLOR.ENDC}"  # return the given text in the given color
warn = lambda s: print(color_str(COLOR.ORANGE, s))  # print the given string as a warning
name_valid = lambda n: n and isinstance(n, str) and n != "Your name here"
student_id_valid = lambda _id: _id and isinstance(_id, int) and 2 * 10**8 <= _id < 10**9
filename_valid = lambda _id: __file__.endswith(f"ECSE211_A2_{_id}.py")
dummy_function_template = lambda *_: _

# End assignment setup. Add additional imports here if needed.
...


NAME = "Alex Tsahageas"
STUDENT_ID = 260958121


# Validate name, student ID, and filename (do not modify)
if not name_valid(NAME):
    warn('Please set your name correctly at the top of this file, eg,\n\nNAME = "Tim Horton"')
    exit(1)
if not student_id_valid(STUDENT_ID):
    warn(f"Please set your student ID correctly at the top of this file, eg,\n\nSTUDENT_ID = 123456789\n")
    exit(1)
if not filename_valid(STUDENT_ID):
    warn(f'Please rename this file to "ECSE211_A2_S.py" where S is your student ID, eg, "ECSE211_A2_123456789.py"')
    exit(1)

print(f"Double check that your student name and ID are correctly set:\nName: {NAME}\nStudent ID: {STUDENT_ID}")


###################################################################
# Part 1 - Review of basic tutorial concepts
###################################################################

# Question 1.1
# Python expression that represents the sum of 2 and 2.

q1_1 = 2+2


# Question 1.2

def q1_2():
    "Print and return the value of the nested sum shown in the assignment description."
    # TODO: Write your code here. Store the value of the sum in the variable "total".
    total = 0
    i = 0
    j = 0
    for i in range(21):
        for j in range(23):
            total += (2*i + j)
    print(f"Q1.2 nested sum is {total}.")
    return total

# Question 1.3

def q1_3():
    "Print and return the a list of length 22 with the pattern [0, -3, 6, -9, ...]."
    result: list[int] = []
    for i in range(22):
        if (i%2==0):
            result.append(3*i)
        else:
            result.append(-3*i)
    print(f"Q1.3 list is {result}.")
    return result

# Question 1.4

def q1_4():
    "Print and return the average hours for the students in the format specified by the assignment description."
    ecse200_hours: dict[str, float] = {"Alpha": 3.5, "Bravo": 6.0, "Charlie": 5.5} 
    ecse202_hours: dict[str, float] = {"Alpha": 4.0, "Bravo": 3.5, "Charlie": 2.0} 
    math141_hours: dict[str, float] = {"Alpha": 5.0, "Bravo": 3.0}  # Charlie does not take this course 
    
    a_hours = 0
    b_hours = 0
    c_hours = 0
    
    a_courses = 0
    b_courses = 0
    c_courses = 0
    
    course_list = [ecse200_hours, ecse202_hours, math141_hours]
    
    for i in course_list:
        if ("Alpha" in i.keys()):
            a_hours += i.get("Alpha")
            a_courses += 1
        if ("Bravo" in i.keys()):
            b_hours += i.get("Bravo")
            b_courses +=  1
        if ("Charlie" in i.keys()):
            c_hours += i.get("Charlie")
            c_courses += 1   

    average_hours: dict[str, float] = {"Alpha": a_hours / a_courses, "Bravo": b_hours / b_courses, "Charlie": c_hours / c_courses}
    print(f"Q1.4 student average hours are {average_hours}.")
    return average_hours
    
    
# Question 1.5  

def calculate_student_avg_hours(courses: list[dict[str, float]]) -> dict[str, float]:
    "Calculate and return student average hours for the given courses."
    names = []
    
    for i in courses:
        for j in i.keys():
            if j not in names:
                names.append(j)
    #print(names)
    
    total_hours = [0] * len(names)
    total_courses = [0] * len(names)
    average_hours_student = [0] * len(names)
    
    for i in courses:
        for x in range(len(names)):
                if names[x] in i.keys():
                    total_hours[x] += i.get(names[x])
                    total_courses[x] += 1
    
    #print(total_hours)
    #print(total_courses)
    
    for j in range(len(names)):
        average_hours_student[j] = total_hours[j] / total_courses[j]
        
    #print(average_hours_student)
    
    average_hours: dict[str, float] = {}
    
    average_hours = dict(zip(names, average_hours_student))
    
    return average_hours

###################################################################
# Part 2 – Find the bugs
###################################################################

# Edit the code that is already there to fix the bugs.

# Question 2.1 (2 bugs)

def q2_1():
    "Print and return the sum of numbers from 0 to 100, inclusive."
    total = 0
    for i in range(101):
        total += i
    print(f"Q2.1 sum is {total}")
    return total


# Question 2.2 (2 bugs)

def q2_2():
    "Print and return welcome_message after replacing all references of '2021' with '2022'."
    welcome_message: str = "Welcome, Class of 2021! Your graduation will take place on October 31, 2021."
    welcome_message = welcome_message.replace("2021", "2022")
    print(f"Q2.2: {welcome_message}")
    return welcome_message


# Question 2.3 (3 bugs)

def find_three_smallest_even(nums: list[int]):
    """
    Return the three smallest even numbers (if any) in the input list, allowing duplicates, eg,

    [1, 2] -> [2]
    [1, 2, 2, 3, 4, 5, 6, 7, 8] -> [2, 2, 4]
    """
    OUTPUT_SIZE = 3
    INF = float("inf")
    nums.sort()
    smallest_numbers: list[int] = []
    used_indices: list[int] = []

    for _ in range(OUTPUT_SIZE):
        smallest_even = INF
        for i in range(len(nums)):
            if i not in used_indices and nums[i] < smallest_even and nums[i] % 2 == 0:
                smallest_even = nums[i]
                used_indices.append(i)
        if smallest_even < INF:
            smallest_numbers.append(smallest_even)

    return smallest_numbers


# Question 2.4 (4 bugs)

def q2_4():
    "Print and return a rainbow of seven color names."
    colors: dict[str, list[float]] = {
        "violet": COLOR.VIOLET,
        "indigo": COLOR.BLUE,
        "blue": COLOR.CYAN,
        "green": COLOR.GREEN,
        "yellow": COLOR.YELLOW,
    }

    # Add missing colors to make a rainbow
    colors.update({
        "orange": COLOR.ORANGE,
        "red": COLOR.RED
    })
    
    #output = []
    output = " "
    
    for color_name, color in colors.items():
        output += f"{color_str(color, color_name)} "
    print(f"Q2.4 rainbow: {output}")
    return output

# ###################################################################
# # Part 3 - Translation from Java
# ###################################################################

class Point:
    "Represents a coordinate point on the playing field grid."
    x = 0.00
    y = 0.00
    _EPSILON = 0.003
    
    def __init__(self, x: float, y: float):
        "Construct a point with the given coordinates."
        self.x = x
        self.y = y

    def distance_to(self, other: Point) -> float:
        "Return the distance between this point and the other point."
        
        dx = float((other.x))- float((self.x))
        dy = float((other.y)) - float((self.y))
        
        return ((dx*dx + dy*dy)**(0.5))

    @staticmethod
    def make_points_from_string(s: str) -> list[Point]:
        "Make points from a string in a comma-separated format, eg '(1,1), (2.5, 3)'."
        result = [] 
        
        if((s == None) or (")" not in s == False)):
            return result

        s = s.replace(" ","").replace("(", "").replace("),",")")
        s = s[0:len(s)-1]
        
        for fragment in s.split(")"):
            xy: list[Point] = fragment.split(",")
            result.append(Point(xy[0],xy[1]))
        return result

    def __eq__(self, o: object) -> bool:
        
        if not isinstance(o, Point):
            return False
        other = Point(0,0)
        other = o
        return (abs((x) - (other.x)) < self._EPSILON and abs(float(y) - float(other.y)) < self._EPSILON)
    
    def __repr__(self) -> str:
        #return "(" + self.x + "," + self.y + ")"
        return "(" + "{:.2f}".format(float(self.x)) + "," + "{:.2f}".format(float(self.y)) + ")"


# End of graded part of the assignment
# 
# ANYTHING YOU ADD BELOW THIS LINE CAN BE USED TO TEST YOUR CODE, BUT WILL NOT BE GRADED.

if __name__ == "__main__":
    """
    Main entry point. After all the code is defined above, the following statements are run to test it.
    Feel free to modify the code below to test your code, it is not graded.
    """
    # Questions 1.1 - 1.4
    print(f"Q1.1: {q1_1}")
    q1_2()
    q1_3()
    q1_4()
    
    # Question 1.5
    courses = [
        ecse200_hours := {"Alpha": 3.5, "Bravo": 2.5, "Charlie": 4},
        math141_hours := {"Alpha": 3},
        ecse202_hours := {"Alpha": 2, "Bravo": 2, "Charlie": 5, "Henry": 6}
        
    ]
    print(f"Q1.5 student average hours are {calculate_student_avg_hours(courses)}")

    # Questions 2.1 - 2.2
    q2_1()
    q2_2()

    # Question 2.3
    print("Q2.3:")
    for test_list in [[], [1, 3, 5], [1, 2], [1, 2, 2, 3, 4, 5, 6, 7, 8]]:
        print(f"find_three_smallest_even({test_list}) = {find_three_smallest_even(test_list)}")

    #Question 2.4
    #try:
    #    q2_4()
    #except ValueError as e:
    #    warn("There is still at least one bug in Q2.4. Fix it to print the rainbow!")

    # Question 3
    # Uncomment the following lines after completing the question to test your Point class
    print("Q3:")
    points = Point.make_points_from_string("(1,1.25), (2,5),(-3, 3)")
    for p in points:
        print(p)
    print(f"The distance between {(p1 := points[0])} and {(p2 := points[1])} is {p1.distance_to(p2):.2f}.")
    
