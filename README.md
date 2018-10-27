# kiwi-travelling-salesman-problem-2018

Introduction
=========

Imagine being an American college student who just wants to visit five amazing European countries on the cheap over her summer break. Finding the flight connections without caring about where exactly she goes to and what is the order should be cool, right?

The problem is, grouping the cities into areas and skipping the ordering requirement makes the problem… well, a lot harder! It's an NP-hard problem that’s been around, in essence, for over 80 years.

Task
=========

Your task is to determine the cheapest possible connection between specific areas, where an area is a set of cities. You’ll be provided with Kiwi.com flight data to help you work out the best algorithm. The solution must meet the following criteria:

- the trip should start from the city we give you
- in each area, you must visit exactly one city (but you can choose which one)
- you have to move between areas every day
- the trip must continue from the same city in which you arrived
- the entire trip should end in the area where it began

Authors
=========

- Helmut Posch
- Martin Žalondek
- Vladimír Šimčák

Slovak University of Technology, Bratislava, Slovakia

Project structure
=========

**- jars**

Runnable jars containing implementation of algorithms which solve Kiwi TSP Challenge 2018 problem. These jars are used in tests.

**java-source/src**

Java source code of Kiwi TSP Challenge 2018

**java-source/src/dtos**

Data transfer objects

**java-source/src/runners**

Classes responsible for running solver, initialize multithreading and collect created results

**java-source/src/solvers**

Implementation of solution finder algorithms

**java-source/src/utils**

Help static utility classes

**test**

Automatized test implemented as python script

 **test/resources/{input, output}**
 
 Test data (input, output)
 
**test/resources/solver-output**
 
 Last output for given input by any solver


References
==========

https://kiwi2018.sphere-contest.com/
