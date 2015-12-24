[![Build Status](https://travis-ci.org/nhl/bootique-linkmove.svg)](https://travis-ci.org/nhl/bootique-linkmove)

# bootique-linkmove

Provides [LinkMove](https://github.com/nhl/link-move) integration with [Bootique](https://github.com/nhl/bootique).

## Note on Cayenne and LinkMove dependency:

_This integration is using an unoffical nightly build of Cayenne 4.0.M3 that gives us Java 8 integration and lots of other good things. It also uses LinkMove that is not yet released to Maven Central. Until Cayenne 4.0.M3 release is out and LinkMove is posted to Central (these are actually related events), you will need to declare an extra repository in your pom.xml (unless you have your own repo proxy, in which case add this repo to the proxy) to have access to this build:_

```XML
<repositories>
    <repository>
        <id>cayenne-unofficial-repo</id>
        <name>Cayenne Unofficial Repo</name>
        <url>http://maven.objectstyle.org/nexus/content/repositories/cayenne-unofficial/</url>
    </repository>
</repositories>
```

