# toolkit/gutenberg-text-cleaner
A python script to clean plain text from [Project Gutenberg](http://www.gutenberg.org) for [word count](../../mapreduce-counter-kotlin/).

## How-To-Use
```bash
$ python3 main filename
```

The structure of plain text from Project Gutenberg is usually like:

```
<HEAD Content>

*** START OF THIS PROJECT GUTENBERG EBOOK <BOOK TITLE> ***

<BOOK CONTENT>

*** END OF THIS PROJECT GUTENBERG EBOOK <BOOK TITLE> ***

<TAIL Content>
```

This script will output `<BOOK CONTENT>` part with only characters and spaces left.
