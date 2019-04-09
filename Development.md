# Development

## Table Of Contents

<!---toc start-->

* [Development](#development)
  * [Table Of Contents](#table-of-contents)
  * [Git workflow](#git-workflow)
    * [master](#master)
    * [develop](#develop)
    * [feature](#feature)
    * [hotfix](#hotfix)
    * [release](#release)
  * [Rules of commits](#rules-of-commits)
  * [Coding standard](#coding-standard)
    * [File naming](#file-naming)
        * [Drawable files](#drawable-files)
        * [Layout files](#layout-files)
        * [Menu files](#menu-files)
        * [Values files](#values-files)
    * [Java language rules](#java-language-rules)
        * [Don't ignore exceptions](#dont-ignore-exceptions)
        * [Don't catch generic exception](#dont-catch-generic-exception)
        * [Don't use finalizers](#dont-use-finalizers)
        * [Fully qualify imports](#fully-qualify-imports)
    * [Java style rules](#java-style-rules)
        * [File and package names](#file-and-package-names)
        * [Names of methods, variables](#names-of-methods-variables)
        * [Comments](#comments)
        * [Use spaces for indentation](#use-spaces-for-indentation)
        * [Use standard brace style](#use-standard-brace-style)
        * [Class member ordering](#class-member-ordering)
        * [Line length limit](#line-length-limit)
        * [Line-wrapping strategies](#line-wrapping-strategies)
        * [The location of the blocks, operators, spaces, brackets](#the-location-of-the-blocks-operators-spaces-brackets)
    * [XML style rules](#xml-style-rules)
        * [Use self closing tags](#use-self-closing-tags)
        * [Resources naming](#resources-naming)
        * [ID naming](#id-naming)
        * [Color naming](#color-naming)
        * [Strings](#strings)
        * [Styles and Themes](#styles-and-themes)
        * [Attributes ordering](#attributes-ordering)

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

For example:
```
#18 Updated README.md, created CodingStandard.md

- Added "Table Of Contents" in README.md
- Moved coding standard to individual file
- Added examples of code in CodingStandard.md
```

## Coding standard

### File naming

#### Drawable files

Naming conventions for drawables:

| Asset Type   | Prefix            |		Example              |
|--------------| ------------------|-----------------------------|
| Action bar   | `ab_`             | `ab_stacked.png`            |
| Button       | `btn_`	           | `btn_send_pressed.png`      |
| Dialog       | `dialog_`         | `dialog_top.png`            |
| Divider      | `divider_`        | `divider_horizontal.png`    |
| Icon         | `ic_`	           | `ic_star.png`               |
| Image        | `img_`            | `img_panda.png`             |
| Menu         | `menu_	`          | `menu_submenu_bg.png`       |
| Notification | `notification_`   | `notification_bg.png`       |
| Tabs         | `tab_`            | `tab_pressed.png`           |

Naming conventions for icons (taken from [Android iconography guidelines](http://developer.android.com/design/style/iconography.html)):

| Asset Type                      | Prefix             | Example                      |
| --------------------------------| ----------------   | ---------------------------- |
| Background Icons                | `ic_`              | `ic_star.png`                |
| Launcher icons                  | `ic_launcher`      | `ic_launcher_calendar.png`   |
| Menu icons and Action Bar icons | `ic_menu`          | `ic_menu_archive.png`        |
| Button icons                    | `ic_btn`           | `ic_btn_recent.png`          |
| Dialog icons                    | `ic_dialog`        | `ic_dialog_info.png`         |

Naming conventions for selector states:

| State	       | Suffix          | Example                     |
|--------------|-----------------|-----------------------------|
| Normal       | `_normal`       | `btn_order_normal.9.png`    |
| Pressed      | `_pressed`      | `btn_order_pressed.9.png`   |
| Focused      | `_focused`      | `btn_order_focused.9.png`   |
| Disabled     | `_disabled`     | `btn_order_disabled.9.png`  |
| Selected     | `_selected`     | `btn_order_selected.9.png`  |

#### Layout files

Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `SignInActivity`, the name of the layout file should be `activity_sign_in.xml`.

| Component        | Class Name             | Layout Name                   |
| ---------------- | ---------------------- | ----------------------------- |
| Activity         | `UserProfileActivity`  | `activity_user_profile.xml`   |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
| Partial layout   | ---                    | `partial_stats_bar.xml`       |

Note that there are cases where these rules will not be possible to apply. For example, when creating layout files that are intended to be part of other layouts. In this case you should use the prefix `partial_`.

#### Menu files

Similar to layout files, menu files should match the name of the component. For example, if we are defining a menu file that is going to be used in the `UserActivity`, then the name of the file should be `activity_user.xml`

A good practice is to not include the word `menu` as part of the name because these files are already located in the `menu` directory.

#### Values files

In accordance with the Android Studio style.

### Java language rules

#### Don't ignore exceptions

You must never do the following:

```java
void setServerPort(String value) {
    try {
        _serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) { }
}
```

_While you may think that your code will never encounter this error condition or that it is not important to handle it, ignoring exceptions like above creates mines in your code for someone else to trip over some day. You must handle every Exception in your code in some principled way. The specific handling varies depending on the case._ - ([Android code style guidelines](https://source.android.com/source/code-style.html))

See alternatives [here](https://source.android.com/source/code-style.html#dont-ignore-exceptions).

#### Don't catch generic exception

You should not do this:

```java
try {
    someComplicatedIOFunction();        // may throw IOException
    someComplicatedParsingFunction();   // may throw ParsingException
    someComplicatedSecurityFunction();  // may throw SecurityException
    // phew, made it all the way
} catch (Exception e) {                 // I'll just catch all exceptions
    handleError();                      // with one generic handler!
}
```

See the reason why and some alternatives [here](https://source.android.com/source/code-style.html#dont-catch-generic-exception)

#### Don't use finalizers

_We don't use finalizers. There are no guarantees as to when a finalizer will be called, or even that it will be called at all. In most cases, you can do what you need from a finalizer with good exception handling. If you absolutely need it, define a `close()` method (or the like) and document exactly when that method needs to be called. See `InputStream` for an example. In this case it is appropriate but not required to print a short log message from the finalizer, as long as it is not expected to flood the logs._ - ([Android code style guidelines](https://source.android.com/source/code-style.html#dont-use-finalizers))

#### Fully qualify imports

This is bad: `import foo.*;`

This is good: `import foo.Bar;`

See more info [here](https://source.android.com/source/code-style.html#fully-qualify-imports)

### Java style rules

#### File and package names

* Only lowercase letters are used in package names

    ```java
    package company.common;
    ```
* Class names must be nouns and the first letters of all words must be uppercase

    ```java
    public class BuildingList {
        ...
    }
    ```

#### Names of methods, variables

* The names of the methods should be verbs, the first letter should be lowercase, the first letters of the inner words should be uppercase

    ```java
    void buildList() {
        ...
    }
    ```
    
* Variable names should begin with a lowercase letter, the first letters of the inner words should be uppercase

    ```java
    int myListSize = 5;
    ```
* Constant names are composed of all uppercase letters separated by the underscore character

    ```java
    final int MY_LIST_MAX_SIZE = 10; 
    ```

* Сlass field names associated with UI should begin with a prefix:

    |      Type    | Prefix |	Example    |
    |--------------| -------|--------------|
    | Button       | `btn`  | `btnSave`    |
    | Dialog       | `dlg`  | `dlgError`   |
    | Text View    | `tv`   | `tvTitle`    |
    | Image View   | `iv`   | `ivResult`   |

* Class field names should begin with a prefix `_`: `_length`, `_btnSave`, `_dlgError`, `_tvTitle`, `_ivResult` and etc.

#### Comments

Сomments should be written to the own classes, class fields and class methods, should be Javadoc.

* Class comments should explain the purpose of the class:

    ```java
    /**
     * Сlass for storing data as a list data structure
     */
    public class BuildingList {
        ...
    }
    ```
    
* Field comments should explain what this field stores:
    
    ```java
    /**
     * Number of elements in list
     */
    int _length;
    ```

* Method comments should explain the purpose of the method:

    ```java
    /**
     * Create an empty list
     *
     * @param length - number of elements
     * @return the resulting list
     */
    BuildingList buildList(int length) {
        ...
    }
    ```

#### Use spaces for indentation

Use __4 space__ indents for blocks:

```java
if (x == 1) {
    x++;
}
```

Use __8 space__ indents for line wraps:

```java
Instrument i =
        someLongExpression(that, wouldNotFit, on, one, line);
```

#### Use standard brace style

Braces go on the same line as the code before them.

```java
class MyClass {
    int func() {
        if (something) {
            ...
        } else if (somethingElse) {
            ...
        } else {
            ...
        }
    }
}
```

Braces around the statements are required unless the condition and the body fit on one line.

If the condition and the body fit on one line and that line is shorter than the max line length, then braces are not required, e.g.

```java
if (condition) body();
```

This is __bad__:

```java
if (condition)
    body();  // bad!
```

#### Class member ordering

There is no single correct solution for this but using a __logical__ and __consistent__ order will improve code learnability and readability. It is recommendable to use the following order:

1. Constants
2. Fields
3. Constructors
4. Override methods and callbacks (public or private)
5. Public methods
6. Private methods
7. Inner classes or interfaces

Example:

```java
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String _title;
    private TextView twTitle;

    @Override
    public void onCreate() {
        ...
    }

    public void setTitle(String title) {
        _title = title;
    }

    private void setUpView() {
        ...
    }

    static class AnInnerClass {

    }

}
```

If your class is extending an __Android component__ such as an Activity or a Fragment, it is a good practice to order the override methods so that they __match the component's lifecycle__. For example, if you have an Activity that implements `onCreate()`, `onDestroy()`, `onPause()` and `onResume()`, then the correct order is:

```java
public class MainActivity extends Activity {

    /**
     * Order matches Activity lifecycle
     */
    @Override
    public void onCreate() {}

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onDestroy() {}

}
```

#### Line length limit

Code lines should not exceed __80 characters__. If the line is longer than this limit there are usually two options to reduce its length:

* Extract a local variable or method (preferable).
* Apply line-wrapping to divide a single line into multiple ones.

There are two __exceptions__ where it is possible to have lines longer than 80:

* Lines that are not possible to split, e.g. long URLs in comments.
* `package` and `import` statements.

#### Line-wrapping strategies

There isn't an exact formula that explains how to line-wrap and quite often different solutions are valid. However there are a few rules that can be applied to common cases.

__Break at operators__

When the line is broken at an operator, the break comes __before__ the operator. For example:

```java
int longName = anotherVeryLongVariable + anEvenLongerOne - thisRidiculousLongOne
        + theFinalOne;
```

__Assignment Operator Exception__

An exception to the `break at operators` rule is the assignment operator `=`, where the line break should happen __after__ the operator.

```java
int longName =
        anotherVeryLongVariable + anEvenLongerOne - thisRidiculousLongOne + theFinalOne;
```

__Method chain case__

When multiple methods are chained in the same line - for example when using Builders - every call to a method should go in its own line, breaking the line before the `.`

```java
Picasso.with(context).load("http://ribot.co.uk/images/sexyjoe.jpg").into(imageView);
```

```java
Picasso.with(context)
        .load("http://ribot.co.uk/images/sexyjoe.jpg")
        .into(imageView);
```

__Long parameters case__

When a method has many parameters or its parameters are very long, we should break the line after every comma `,`

```java
loadPicture(context, "http://ribot.co.uk/images/sexyjoe.jpg", mImageViewProfilePicture, clickListener, "Title of the picture");
```

```java
loadPicture(context,
        "http://ribot.co.uk/images/sexyjoe.jpg",
        mImageViewProfilePicture,
        clickListener,
        "Title of the picture");
```

#### The location of the blocks, operators, spaces, brackets

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
   
* There is one empty line between the methods

   ```java
   public class MainActivity extends Activity {

       public void setTitle(String title) {
           _title = title;
       }

       private void setUpView() {
           ...
       }

   }
   ```

* Any operator should be surrounded with spaces

   ```java
   res = getCount();
   (a > 10) ? b : c;
   ```
   
### XML style rules

#### Use self closing tags

When an XML element doesn't have any contents, you __must__ use self closing tags.

This is good:

```xml
<TextView
    android:id="@+id/text_view_profile"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

This is __bad__ :

```xml
<!-- Don\'t do this! -->
<TextView
    android:id="@+id/text_view_profile"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" >
</TextView>
```

#### Resources naming

Resource IDs and names are written in __lowercase_underscore__.

#### ID naming

IDs should be prefixed with the name of the element in lowercase underscore. For example:


| Element            | Prefix            |
| -----------------  | ----------------- |
| `TextView`         | `text_view_`      |
| `ImageView`        | `image_view_`     |
| `Button`           | `button_`         |
| `Menu`             | `menu_`           |

Image view example:

```xml
<ImageView
    android:id="@+id/image_view_profile"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

Menu example:

```xml
<menu>
    <item
        android:id="@+id/menu_done"
        android:title="Done" />
</menu>
```

#### Color naming

Сolors should be named after their color, not where they are used: `colorLightGrey`.

There is __exception__ where it is possible to name colors after where they are used:

* Сolors refer to the main theme: `colorPrimary`, `textColorPrimary`, `colorPrimaryDark`, `windowBackground`, `navigationBarColor` and etc.

#### Strings

String names start with a prefix that identifies the section they belong to. For example `registration_email_hint` or `registration_name_hint`. If a string __doesn't belong__ to any section, then you should follow the rules below:

| Prefix             | Description                           |
| -----------------  | --------------------------------------|
| `error_`           | An error message                      |
| `msg_`             | A regular information message         |
| `title_`           | A title, i.e. a dialog title          |
| `action_`          | An action such as "Save" or "Create"  |

#### Styles and Themes

Unlike the rest of resources, style names are written in __UpperCamelCase__.

#### Attributes ordering

As a general rule you should try to group similar attributes together. A good way of ordering the most common attributes is:

1. View Id
2. Style
3. Layout width and layout height
4. Other layout attributes, sorted alphabetically
5. Remaining attributes, sorted alphabetically
