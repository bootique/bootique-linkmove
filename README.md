<!--
  Licensed to ObjectStyle LLC under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ObjectStyle LLC licenses
  this file to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

[![Build Status](https://travis-ci.org/bootique/bootique-linkmove.svg)](https://travis-ci.org/bootique/bootique-linkmove)
[![Maven Central](https://img.shields.io/maven-central/v/io.bootique.linkmove/bootique-linkmove.svg?colorB=brightgreen)](https://search.maven.org/artifact/io.bootique.linkmove/bootique-linkmove/)

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
            <version>1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove</artifactId>
</dependency>

<!-- If you need to process JSON sources also include JSON module-->
<!-- 
<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove-json</artifactId>
</dependency> 
-->
```

## Use LinkMove

Now you can inject `LmRuntime` in your code, build and execute `LmTasks`. 

## Example Project

[bootique-linkmove-demo](https://github.com/bootique-examples/bootique-linkmove-demo)

