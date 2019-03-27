package br.com.concrete.tentacle.features.menu

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.loadRoundImageUrl
import br.com.concrete.tentacle.features.login.LoginActivity
import br.com.concrete.tentacle.features.profile.ProfileActivity
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.fragment_menu.camera
import kotlinx.android.synthetic.main.fragment_menu.iconProfile
import kotlinx.android.synthetic.main.fragment_menu.logout
import kotlinx.android.synthetic.main.fragment_menu.name
import kotlinx.android.synthetic.main.fragment_menu.profile
import kotlinx.android.synthetic.main.fragment_menu.state
import kotlinx.android.synthetic.main.fragment_menu.version
import org.koin.android.viewmodel.ext.android.viewModel
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

private const val REQUEST_CODE_READ_EXTERNAL = 0
private const val REQUEST_CODE_WRITE_EXTERNAL = 1

class MenuFragment : Fragment() {

    private val menuViewModel: MenuViewModel by viewModel()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initObservable()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservable() {
        menuViewModel.getUser().observe(this, Observer { base ->
            base.getContentIfNotHandler()?.let { stateModel ->
                when (stateModel.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        stateModel.model?.let { updateUI(it) }
                    }
                    else -> stateModel.errors?.message?.let {
                        LogWrapper.log("A problem happens", it[0])
                    }
                }
            }
        })

        lifecycle.addObserver(menuViewModel)
    }

    fun init() {
        logout.setOnClickListener { checkLogout() }
        profile.setOnClickListener { goToProfile() }
        version.text = String.format(getString(R.string.version), BuildConfig.VERSION_NAME)
        configCamera()
        setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READ_EXTERNAL)
    }

    private fun configCamera() {
        EasyImage.configuration(context)
            .setImagesFolderName("Tentacle")
            .saveInAppExternalFilesDir()
            .setCopyExistingPicturesToPublicLocation(true)
            .saveInRootPicturesDirectory()

        camera.setOnClickListener {
            setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)
            EasyImage.openChooserWithGallery(this, getString(R.string.select_hint), EasyImage.REQ_SOURCE_CHOOSER)
        }
    }

    private fun goToProfile() {
        activity?.launchActivity<ProfileActivity>(
            animation = ActivityAnimation.TRANSLATE_UP
        )
    }

    private fun updateUI(u: User) {
        user = u
        name.text = user.name
        state.text = user.state.name
        loadPhotoFile()
    }

    private fun loadPhotoFile() {
        try {
            val file = File(menuViewModel.getPathPhotoUser(user._id))
            setUpImage(file)
        } catch (e: Exception) {
            LogWrapper.log("File Error:", e.toString())
        }
    }

    private fun checkLogout() {
        activity?.let {
            DialogUtils.showDialog(
                context = it,
                title = getString(R.string.logout_title),
                message = getString(R.string.logout_question),
                positiveText = getString(R.string.ok),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    performLogout()
                },
                negativeText = getString(R.string.cancel)
            )
        }
    }

    private fun performLogout() {
        menuViewModel.removeSession()
        menuViewModel.removeUser()
        val login = Intent(activity, LoginActivity::class.java)
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(login)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                menuViewModel.savePathUserPhoto(user._id, imageFile?.path!!)
                setUpImage(imageFile)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                LogWrapper.log("Error: ", "Picking image: ${e.toString()}")
            }

            override fun onCanceled(source: EasyImage.ImageSource, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity)
                    photoFile?.delete()
                }
            }
        })
    }

    private fun setUpImage(imagesFiles: File?) {
        imagesFiles?.let {
            if (it.exists()) {
                iconProfile.loadRoundImageUrl(it.absolutePath)
                user.internalImage = it.absolutePath
                menuViewModel.updateUser(user)
                LogWrapper.log("PATH: ", it.absolutePath)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL -> externalPermission(grantResults, permissions)
            REQUEST_CODE_WRITE_EXTERNAL -> externalPermission(grantResults, permissions)
        }
    }

    private fun externalPermission(grantResults: IntArray, permissions: Array<out String>) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LogWrapper.log("TAG", "Permission: " + permissions[0] + "was " + grantResults[0])
        }
    }

    private fun setPermissions(permissions: String, requestCode: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(activity!!, permissions) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(permissions), requestCode)
                false
            }
        } else {
            true
        }
    }
}
