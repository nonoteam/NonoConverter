# NonoConverter
Converts image to nonogram

## Git workflow
* master - branch with releases
* develop - branch created from master, with last changes
* feature - branches for adding new functionalities; name: "NC-number_issue"
* hotfix - branches created from master for hot fixes; name: "NC-number_issue-fix-number_fix"
* release - branches created from develop for preparing for release of new product versions; name: "release-number_sprint"

## Rules of commits
* Commit message consists of a subject line (≤ 50 characters) and body text (optional)
* The subject line: start with "#issue_number"(if it exists) followed by verb in past form starting with capital letter and don't end with dot
* The title and body text are separated with empty line
* The body text can contain several lines, each started with '-', altogether representing the list of changes: what and why (not how)

## Coding standard
### File and package names
* Only lowercase letters are used in package names
* Class names must be nouns and the first letters of all words must be uppercase
### Names of methods, variables
* The names of the methods should be verbs, the first letter should be lowercase, the first letters of the inner words should be uppercase
* Variable names should begin with a lowercase letter, the first letters of the inner words should be uppercase
* Constant names are composed of all uppercase letters separated by the underscore character
### Line length, line breaks
* The length of the line should not exceed 80 characters
* If the length of the expression exceeds the length of the line, it must be split into several lines
### The location of the blocks, operators, spaces, brackets
* Variable definitions should be located at the beginning of the block. Variables should be initialized as soon as possible
* There is no space between the method name and the brackets for the parameter list
* Parameters are separated by a space
* Any operator should be surrounded with spaces

## Development team
* Maxim Smolskiy - teamlead
* Mikhail Nakhatovich - techlead
* Pavel Melnik
* Nikita Sukhanov
* Ilya Surikov
