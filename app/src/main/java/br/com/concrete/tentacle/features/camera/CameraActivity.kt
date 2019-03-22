package br.com.concrete.tentacle.features.camera

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.cameraPermissionResultGranted
import br.com.concrete.tentacle.extensions.compress
import br.com.concrete.tentacle.extensions.hasCameraPermission
import br.com.concrete.tentacle.extensions.requestCameraPermission
import br.com.concrete.tentacle.extensions.rotate
import br.com.concrete.tentacle.extensions.visible
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.front
import kotlinx.android.synthetic.main.activity_camera.btAccept
import kotlinx.android.synthetic.main.activity_camera.btReject
import kotlinx.android.synthetic.main.activity_camera.btTakePicture
import kotlinx.android.synthetic.main.activity_camera.cameraView
import kotlinx.android.synthetic.main.activity_camera.ivPreview
import java.io.File
import java.io.FileOutputStream

class CameraActivity : BaseActivity() {

    private var fotoApparat: Fotoapparat? = null
    private var photoAccepted: Bitmap? = null
    private var permissionsGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        checkCameraPermission()
        init()
    }

    private fun checkCameraPermission(){
        permissionsGranted = hasCameraPermission()
        if(permissionsGranted){
            cameraView.visible(true)
        }else{
            cameraView.visible(false)
            requestCameraPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        if(permissionsGranted){
            fotoApparat?.start()
        }
    }

    override fun onStop() {
        if(permissionsGranted){
            fotoApparat?.stop()
        }
        super.onStop()
    }

    private fun init(){
        fotoApparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = front()
        )

        btTakePicture.setOnClickListener {
            takePicture()
        }

        btAccept.setOnClickListener {
            savePicture()
            finish()
        }

        btReject.setOnClickListener {
            photoAccepted = null
            showPreview(false)
        }
    }

    private fun takePicture(){
        fotoApparat?.let {
            val result = it.takePicture()

            result
                .toBitmap()
                .whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.let {photo ->
                        photoAccepted = photo.bitmap.rotate(-photo.rotationDegrees.toFloat())
                        ivPreview.setImageBitmap(photoAccepted)
                        showPreview(true)
                    }
                }

        }
    }

    private fun showPreview(show: Boolean){
        ivPreview.visible(show)
        btAccept.visible(show)
        btReject.visible(show)

        cameraView.visible(!show)
        btTakePicture.visibility = if(show) View.INVISIBLE else View.VISIBLE
    }

    private fun savePicture(){
        photoAccepted?.let { bmp ->
            val fileName = "tentacle_profile_picture.jpg"
            val file = File(filesDir, fileName)
            val fos = FileOutputStream(file)
            fos.write(bmp.compress(50))
            fos.flush()
            fos.close()
        }
    }

    override fun getFinishActivityTransition() = ActivityAnimation.TRANSLATE_DOWN

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(cameraPermissionResultGranted(requestCode, permissions, grantResults)){
            permissionsGranted = true
            fotoApparat?.start()
            cameraView.visible(true)
        }
    }
}