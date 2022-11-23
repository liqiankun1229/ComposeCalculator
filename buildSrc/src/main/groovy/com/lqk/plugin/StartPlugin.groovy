package com.lqk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class StartPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.afterEvaluate {

            println("开始编译")
        }

    }
}