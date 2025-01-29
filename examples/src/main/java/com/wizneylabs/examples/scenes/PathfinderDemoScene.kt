package com.wizneylabs.examples.scenes

import com.wizneylabs.kollie.pathfinder.GridRenderer
import com.wizneylabs.kollie.core.Scene

import com.wizneylabs.examples.components.BasicComponentExample

class PathfinderDemoScene: Scene() {

    override fun Start() {

        // entity A
        val entityA = this.AddEntity("basicComponent");

        val componentA = entityA.AddComponent<BasicComponentExample>();

        componentA.name = "bob";

        // entity B
        val entityB = this.AddEntity("basicComponent");

        val componentB = entityB.AddComponent<BasicComponentExample>();

        componentB.name = "jane";
    }
}