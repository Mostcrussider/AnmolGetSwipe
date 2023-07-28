package anmol.bansal.anmolgetswipe.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.hypot

object CommonUtils {

    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackBarWithAction(
        view: View,
        message: String,
        actionText: String,
        actionListener: View.OnClickListener
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction(actionText, actionListener)
            .show()
    }

    fun hideKeyboard(activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity.currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    fun Fragment.hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun resizeAndCompressImage(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val desiredWidth = 100
        val desiredHeight = 100
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true)

        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        val file = File(context.cacheDir, "temp_image.jpg")
        val fileOutputStream = FileOutputStream(file)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        return file
    }

    fun View.scaleDownToFab() {
        val scaleDown = ScaleAnimation(
            1f, 0f,
            1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 300 // Adjust the duration as needed
            interpolator = AccelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    visibility = View.INVISIBLE
                }
            })
        }

        startAnimation(scaleDown)
    }

}


/* fun ContentResolver.resizeAndCropImage(uri: Uri, targetSize: Int): Bitmap? {
     return try {
         val inputStream: InputStream = this.openInputStream(uri) ?: return null
         val options = BitmapFactory.Options().apply {
             inJustDecodeBounds = true
         }
         BitmapFactory.decodeStream(inputStream, null, options)
         inputStream.close()

         val imageHeight = options.outHeight
         val imageWidth = options.outWidth
         val imageSize = if (imageWidth > imageHeight) imageWidth else imageHeight
         val scale = imageSize.toFloat() / targetSize.toFloat()

         val inputStream2: InputStream = this.openInputStream(uri) ?: return null
         val scaledBitmap = BitmapFactory.decodeStream(inputStream2, null, BitmapFactory.Options().apply {
             inSampleSize = calculateInSampleSize(options, (targetSize * scale).toInt(), (targetSize * scale).toInt())
         })
         inputStream2.close()

         if (scaledBitmap!=null){
             val x = (scaledBitmap.width - targetSize) / 2
             val y = (scaledBitmap.height - targetSize) / 2
             Bitmap.createBitmap(scaledBitmap, x, y, targetSize, targetSize)
         } else {
             null
         }
     } catch (e: Exception) {
         null
     }
 }

 private fun calculateInSampleSize(
     options: BitmapFactory.Options,
     reqWidth: Int,
     reqHeight: Int
 ): Int {
     val (height: Int, width: Int) = options.run { outHeight to outWidth }
     var inSampleSize = 1
     if (height > reqHeight || width > reqWidth) {
         val halfHeight: Int = height / 2
         val halfWidth: Int = width / 2
         while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
             inSampleSize *= 2
         }
     }
     return inSampleSize
 }*/