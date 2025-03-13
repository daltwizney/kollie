package com.wizneylabs.examples

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.SharedCamera

import com.wizneylabs.examples.ui.theme.KollieTheme
import com.wizneylabs.examples.viewmodels.MainViewModel
import com.wizneylabs.kollie.composables.KollieCanvas

class MainActivity : ComponentActivity() {

    private var _hasCameraPermission = false;

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        _hasCameraPermission = isGranted;
    }

    private val _mainViewModel: MainViewModel by viewModels();

    val camera: SharedCamera?
        get() = _camera;

    val cameraId: String
        get() = _cameraId;

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

                if (_mainViewModel.arSession.value != null)
                {
                    KollieCanvas(_mainViewModel.arSession.value!!);
                }
                else
                {
                    Text("loading AR Session...");
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val currentSession = _mainViewModel.arSession.value;

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

        _mainViewModel.arSession.value?.pause();
    }

    override fun onDestroy() {
        super.onDestroy()

        _mainViewModel.arSession.value?.close();
    }

    fun createSession() {

        Log.i("ExamplesMainActivity", "creating AR session!");

        _mainViewModel.arSession.value = Session(this);

        val config = Config(_mainViewModel.arSession.value);

//        config.setFocusMode(Config.FocusMode.AUTO);
//        config.setUpdateMode(Config.UpdateMode.BLOCKING);

        _mainViewModel.arSession.value?.configure(config);

        _mainViewModel.arSession.value?.resume();

//        _camera = _arSession?.sharedCamera;
//        _cameraId = _arSession?.getCameraConfig()?.getCameraId() ?: "";
    }
}