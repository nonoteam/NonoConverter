# NonoConverter
Converts image to nonogram

## Table Of Contents

<!---toc start-->

* [NonoConverter](#nonoconverter)
  * [Table Of Contents](#table-of-contents)
  * [Git workflow](#git-workflow)
    * [master](#master)
    * [develop](#develop)
    * [feature](#feature)
    * [hotfix](#hotfix)
    * [release](#release)
  * [Rules of commits](#rules-of-commits)
  * [Coding standard](http://github.com/nonoteam/NonoConverter/docs/CodingStandard.md)
  * [Development team](#development-team)

<!---toc end-->

## Git workflow
### master
* Branch with releases
### develop
* Branch created from master, with last changes
### feature
* Branches created from develop for adding new functionalities
* Name: `NC-number_issue`, for example: `NC-1`
### hotfix
* Branches created from master and develop for hot fixes
* Name: `NC-number_issue-fix-number_fix`, for example: `NC-1-fix-1`
### release
* Branches created from develop for preparing for release of new product versions. After preparing branches are merged into master and develop
* Name: `release-number_version`, for example: `release-1.0`

## Rules of commits
* Commit message consists of a subject line (≤ 50 characters) and body text (optional)
* The subject line: start with `#issue_number`(for example: `#1`) followed by verb in past form starting with capital letter and don't end with dot
* The title and body text are separated with empty line
* The body text can contain several lines, each started with `-` and capital letter, altogether representing the list of changes: what and why (not how)

## Development team
* [Maxim Smolskiy](https://github.com/MaximSmolskiy) - **teamlead**
* [Mikhail Nakhatovich](https://github.com/MikhailNakhatovich) - **techlead**
* [Pavel Melnik](https://github.com/PawelMelnik)
* [Nikita Sukhanov](https://github.com/NikitaSukhanov)
* [Ilya Surikov](https://github.com/Chopikov)
