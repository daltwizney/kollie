package com.wizneylabs.freestyle.composables

import kotlinx.serialization.Serializable

/********************************************************************
 ************************* Navigation Routes ************************
 ********************************************************************/

@Serializable
object HomeRoute;

@Serializable
data class FreestyleEditorRoute(val shaderID: String);
