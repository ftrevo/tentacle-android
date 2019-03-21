package br.com.concrete.tentacle.features.menu

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.loadRoundImageBitmap
import br.com.concrete.tentacle.extensions.loadRoundImageResource
import br.com.concrete.tentacle.extensions.loadRoundImageUrl
import br.com.concrete.tentacle.features.camera.CameraActivity
import br.com.concrete.tentacle.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_menu.ivChangePhoto
import kotlinx.android.synthetic.main.fragment_menu.ivProfile
import java.io.File


class MenuFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()

        fetchProfilePicture()
    }

    private fun init(){
        ivChangePhoto.setOnClickListener {
            showPictureDialog()
        }
    }
    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    private fun showPictureDialog(){
        context?.let{
            DialogUtils.showDialog(
                context = it,
                title = "Foto",
                message = "Escolha de onde você quer pegar uma foto",
                positiveText = "Câmera",
                negativeText = "Galeria",
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    showCamera()
                },
                negativeListener = DialogInterface.OnClickListener { _, _ ->

                }
            )
        }
    }

    private fun showCamera(){
        if(activity is AppCompatActivity){
            activity!!.launchActivity<CameraActivity>(animation = ActivityAnimation.TRANSLATE_UP)
        }
    }

    private fun fetchProfilePicture(){
        val fileName = "tentacle_profile_picture.jpg"
        val file = File(activity!!.filesDir, fileName)
        if(file.exists()){
            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            ivProfile.loadRoundImageBitmap(bmp)
        }else{
            ivProfile.loadRoundImageResource(R.drawable.ic_account)
        }
    }
}
