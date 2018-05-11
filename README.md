[![Build Status](https://travis-ci.org/bootique/bootique-linkmove.svg)](https://travis-ci.org/bootique/bootique-linkmove)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.bootique.linkmove/bootique-linkmove/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.bootique.linkmove/bootique-linkmove/)

# bootique-linkmove

Provides [LinkMove](https://github.com/nhl/link-move) integration with [Bootique](http://bootique.io).

*For additional help/questions about this example send a message to
[Bootique forum](https://groups.google.com/forum/#!forum/bootique-user).*
   
## Prerequisites
      
    * Java 1.8 or newer.
    * Apache Maven.
      
# Setup

## Add bootique-linkmove to your build tool
**Maven**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.bootique.bom</groupId>
            <artifactId>bootique-bom</artifactId>
            <version>0.25</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove</artifactId>
</dependency>
```

## Example Project

[bootique-linkmove-demo](https://github.com/bootique-examples/bootique-linkmove-demo)

