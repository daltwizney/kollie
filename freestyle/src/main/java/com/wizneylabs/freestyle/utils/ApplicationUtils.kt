package com.wizneylabs.freestyle.utils

import android.app.Application
import android.util.Log

class ApplicationUtils {

    companion object {

        val TAG = ApplicationUtils::class.simpleName;

        fun loadShaderFromAssets(application: Application, fileName: String): String {

            var shaderCode = "";

            try {
                shaderCode = application.assets.open(fileName)
                    .bufferedReader().use { it.readText() };
            }
            catch (e: Exception) {

                Log.e(TAG, "failed to load shader: ${fileName}");
            }

            return shaderCode;
        }
    }
}