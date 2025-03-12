package com.wizneylabs.examples

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.SharedCamera

import com.wizneylabs.examples.ui.theme.KollieTheme
import com.wizneylabs.kollie.composables.KollieCanvas

class MainActivity : ComponentActivity() {

    private var _arSession: Session? = null;

    private var _hasCameraPermission = false;

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        _hasCameraPermission = isGranted;
    }

    private var _camera: SharedCamera? = null;
    private var _cameraId: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d("ExamplesMainActivity", "loading AR Core...");

        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            Log.d("ExamplesMainActivity", "availability isSupported = ${availability.isSupported}");
        }

        setContent {

//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

            KollieTheme {
                KollieCanvas()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val currentSession = _arSession;

        if (currentSession != null)
        {
            Log.i("ExamplesMainActivity", "resuming session");
            currentSession.resume();
        }
        else
        {
            // TODO: should verify "Google Play Services for AR" is installed!

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

                _hasCameraPermission = true;
            }
            else
            {
                Log.i("ExamplesMainActivity", "requesting camera permission!");
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }

            // TODO: on fresh installs, this will be false the first time b.c.
            // we need to wait until requestPermissionLauncher coroutine completes!
            if (_hasCameraPermission)
            {
                this.createSession();
            }
        }
    }

    override fun onPause() {
        super.onPause()

        Log.i("ExamplesMainActivity", "pausing session!");

        _arSession?.pause();
    }

    override fun onDestroy() {
        super.onDestroy()

        _arSession?.close();
    }

    fun createSession() {

        Log.i("ExamplesMainActivity", "creating AR session!");

        _arSession = Session(this);

        val config = Config(_arSession);

        _arSession?.configure(config);

//        _camera = _arSession?.sharedCamera;
//        _cameraId = _arSession?.getCameraConfig()?.getCameraId() ?: "";
    }
}