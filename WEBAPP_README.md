# Using Graphviz from the web app

Add Graphviz buildpack in addition to java buildpack so that heroku understands it is a Java app but it also knows it needs to install graphviz.

Graphviz buildpack: https://github.com/weibeld/heroku-buildpack-graphviz
Java buildpack: https://github.com/heroku/heroku-buildpack-java

## How to use multiple buildpacks:
Execute these before pushing to heroku:

```
  heroku buildpacks:add --index 1 https://github.com/heroku/heroku-buildpack-java
  heroku buildpacks:add --index 2 https://github.com/weibeld/heroku-buildpack-graphviz.git
```

# Viz.js

- Download https://github.com/mdaines/viz.js/releases
- Project https://github.com/mdaines/viz.js/


# Escaping dot result for Viz.js

Here we needed to get the unescaped HTML for dot result.
So we used the three curly braces instead of two.
Otherwise, instead of "->" we get "-&gt" (greater than symbol is HTML escaped).

For more information, see: https://mustache.github.io/mustache.5.html

Also, do not use quotes, newlines, tabs in dot result. Otherwise, dot text is broken into multiple html elements
and graph cannot be created.
