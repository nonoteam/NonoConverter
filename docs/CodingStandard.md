# Coding standard
## File and package names
* Only lowercase letters are used in package names
* Class names must be nouns and the first letters of all words must be uppercase
## Names of methods, variables
* The names of the methods should be verbs, the first letter should be lowercase, the first letters of the inner words should be uppercase
* Variable names should begin with a lowercase letter, the first letters of the inner words should be uppercase
* Constant names are composed of all uppercase letters separated by the underscore character
## Line length, line breaks
* The length of the line should not exceed 80 characters
* If the length of the expression exceeds the length of the line, it must be split into several lines


    ```java
    int result = function1(longExpression1,
       function2(longExpression2,
       longExpression3));
    ```   
## The location of the blocks, operators, spaces, brackets
* Variable definitions should be located at the beginning of the block. Variables should be initialized as soon as possible


   ```java
   void myMethod() {
       int count = 0; // beginning of method block
       if (condition) {
           int int2; // beginning of "if" block
           ...
       }
    }
    ```
* There is no space between the method name and the brackets for the parameter list


   ```java
   getStaffList(String name)
   ```
   
* Parameters are separated by a space
 
   ```java
   getStaffList(String name, int count)
   ```
   
* Any operator should be surrounded with spaces

   ```java
   res = getCount();
   (a > 10) ? b : c;
   ```
