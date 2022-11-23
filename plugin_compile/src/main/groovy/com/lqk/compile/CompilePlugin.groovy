package com.lqk.compile

import org.gradle.api.Plugin
import org.gradle.api.Project

class CompilePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.afterEvaluate {
            println("CompilePlugin")
        }
    }
}