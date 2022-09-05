# Description

This program takes in a list of employees,  what positions they work, how many hours they work, and what their days off are, along with a list of shifts that need to be filled daily, then generates a schedule using a genetic algorithim. The schedules are graded based off how many shifts are filled, and how many people are in their best position (aces in their places).

## File Imports
In order to have the program properly use the employee and shift list, there must be a text file labeled "Employees" and "Shifts" respectively, that are properly formatted.


**Empolyees**


The proper format is: Last name, First name chars representing positions seperated by commas, amount of hours to work in week, M for if they are a minor, day of the week the cant work (O,T,W,R,F,S,Z for Monday-Sunday)


Example:
Doe, Jane O,S,T,G,E 17.5 M O,R
A minor who cannot work Mondays or Thursdays, and knows the positions represented by O, S, T, G, and E.

The first position of the list the employee can work must be the position they are best at. This will help determain which schedules are better than others.
Add blank lines in order to represent where the breaks in management/other positions are.


**Shifts**


The proper format is: Char representing position, the time of which the positon occurs, how long the position is in hours (represented by a double)
Example:
G 7:30a-3:30p 8.0
L 3:30p-11:00p 7.5

The positions that the least number of people can fill should be first, so they can be first piority to fill. 

