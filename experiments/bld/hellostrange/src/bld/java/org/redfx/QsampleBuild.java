package org.redfx;

import rife.bld.Project;

import java.util.List;

import rife.bld.dependencies.Repository;
import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public class QsampleBuild extends Project {
    public QsampleBuild() {
        pkg = "org.redfx";
        name = "Qsample";
        mainClass = "org.redfx.QsampleMain";
        version = version(0,1,0);

        downloadSources = true;
        autoDownloadPurge = true;
        Repository ossh = new Repository("https://oss.sonatype.org/content/repositories/snapshots/");
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES, SONATYPE_SNAPSHOTS, ossh);
        scope(compile)
            .include(dependency("org.openjfx", "javafx-controls", version(23,0,1)))
            .include(dependency("org.redfx", "strange", version(0,2,0, "SNAPSHOT")))
            .include(dependency("org.redfx", "strangefx", version(0,1,4)));
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,11,0)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,11,0)));
    }

    public static void main(String[] args) {
        new QsampleBuild().start(args);
    }
}
