package com.shoppingfoodcart.firstassigment.utils.application


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

const val USER_TYPE = "user"
const val TYRE_NAME = "Tyre replacing"
const val HEIGHT = "Height"
const val DIAMETER = "diameter"
const val WIDTH = "Width"
const val OPEN = "open"
const val START = "start"
const val ACCEPTED = "accepted"
const val HEADING = "heading"
const val REACHED = "reached"
const val AMOUNT = "amount"
const val PERCENTAGE = "percentage"
const val CANCELLED = "cancelled"
const val COMPLETED = "completed"
const val LOC_PICKER_CODE = 1001



fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun ImageView.loadImage(url: String) {
    val circularProgressDrawable = context?.let { CircularProgressDrawable(it) }
    circularProgressDrawable?.strokeWidth = 5f
    circularProgressDrawable?.centerRadius = 30f
    circularProgressDrawable?.start()
    context?.let {
        Glide.with(it)
            .load(url)
/*                .placeholder(R.drawable.place_holder_logo)*/
            .into(this)
    }

}

fun ImageView.loadImage(resourceID: Int) {
    Glide.with(context)
        .load(resourceID)
        .into(this)
}

fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags =
        if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}


fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T> : Callback<T> {

    var onSucessResponse: ((Response<T>) -> Unit)? = null
    var onErrorResponse: ((String?) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            if (response.body() != null) {
                onSucessResponse?.invoke(response)
            } else if (response.errorBody() != null) {
                onErrorResponse?.invoke(response.errorBody()?.string())
            } else {
                onFailure?.invoke(Throwable("No error mentioned"))
            }

        } else {
            onErrorResponse?.invoke(response.errorBody()?.string())
        }
    }

}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisibleToUser(): Boolean {
    return visibility == View.VISIBLE
}

fun EditText.string(): String {
    return this.text.toString().trim()
}

fun String?.requireString(): String {
    return "" + this
}

fun ImageView.setImageTint(color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color),
        android.graphics.PorterDuff.Mode.SRC_IN
    );
}


fun Context.newNavigatorIntent(
    latitude: Double,
    longitude: Double,
    name: String
): Intent? {
    val format =
        "geo:0,0?q=" + java.lang.Double.toString(latitude) + "," + java.lang.Double.toString(
            longitude
        ) + "(" + name + ")"
    val uri: Uri = Uri.parse(format)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    return if (intent.resolveActivity(this.packageManager) != null) {
        intent
    } else null
}


fun Context.newDialerIntent(phone: String): Intent? {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phone")
    return if (intent.resolveActivity(this.packageManager) != null) {
        intent
    } else null
}


fun Context.newSendEmailsIntent(
    emails: Array<String>?,
    subject: String?,
    text: String?
): Intent? {
    val mailIntent = Intent(Intent.ACTION_SENDTO)
    mailIntent.type = "message/rfc822"
    mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    mailIntent.putExtra(Intent.EXTRA_EMAIL, emails)
    mailIntent.putExtra(Intent.EXTRA_TEXT, text)
    return if (mailIntent.resolveActivity(this.packageManager) != null) {
        mailIntent
    } else null
}

fun Context.newSendEmailIntent(
    email: String,
    subject: String?,
    text: String?
): Intent? {
    return newSendEmailsIntent(arrayOf(email), subject, text)
}

fun Context.newOpenUrlIntent(url: String?): Intent? {
    val urlIntent = Intent(Intent.ACTION_VIEW)
    urlIntent.data = Uri.parse(url)
    return if (urlIntent.resolveActivity(this.packageManager) != null) {
        urlIntent
    } else null
}

fun Context.newShareFileIntent(
    uri: Uri?,
    mimeType: String?
): Intent? {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.type = mimeType
    return if (shareIntent.resolveActivity(this.packageManager) != null) {
        shareIntent
    } else null
}


fun Context.newShareTextIntent(text: String?): Intent? {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.putExtra(Intent.EXTRA_TEXT, text)
    shareIntent.type = "text/*"
    return if (shareIntent.resolveActivity(this.packageManager) != null) {
        shareIntent
    } else null
}


fun Activity.openActivity(clazz: Class<out Activity>) {
    startActivity(Intent(this, clazz))
}

fun Activity.openActivityWithExist(clazz: Class<out Activity>) {
    startActivity(Intent(this, clazz))
    this.finish()
}


fun View.collapse() {
    val animate = TranslateAnimation(
        0f,
        0f,
        0f,
        this.height.toFloat()
    )
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            this@collapse.gone()
        }

        override fun onAnimationStart(animation: Animation?) {

        }
    })
}


fun View.expand() {
    val animate = TranslateAnimation(
        0f,
        0f,
        this.height.toFloat(),
        0f
    )
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {

        }

        override fun onAnimationStart(animation: Animation?) {
            this@expand.visible()
        }
    })
}


fun View.showSnackBar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackbar.show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}


fun Fragment.getSimpleName(): String {
    return this.javaClass.simpleName
}

fun Fragment.getColorCustom(color: Int): Int {
    return ContextCompat.getColor(requireContext(), color)
}


fun Date.dateWithUTCTimeZone(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    return sdf.format(this)
}

fun Date.dateWithCurrentTimeZone(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    sdf.setTimeZone(TimeZone.getDefault());
    return sdf.format(this)
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentDate = sdf.format(Date())
    System.out.println(" C DATE is  $currentDate")
    return currentDate
}

fun TextView.changeTimeFormat(): String {
    val parser = SimpleDateFormat("dd MMM, yyyy hh:mm aa")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(parser.parse(this.text.toString()))
}

fun String.changeDateTimeFormat(oldFormat: String, newFormat: String): String {
    val parser = SimpleDateFormat(oldFormat)
    val formatter = SimpleDateFormat(newFormat)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(parser.parse(this))
}


fun String.splitString(): List<String>? = this.split(",").map { it.trim() }


fun TextView.subTotal(serviceCharge: Double, itemCharge: Int, discount: Double): Double {
    var totalSum = (serviceCharge.plus(itemCharge)).minus(discount)
    this.text = "£ $totalSum"
    return totalSum
}

fun TextView.calculateTotal(subTotal: Double, vat: Double): Double {
    var totalSum = subTotal.plus(vat)
    this.text = "£ $totalSum"
    return totalSum
}

fun TextView.setVat(subTotal: Double, value: Int): Double {
    var vatAmount = 0.0
    if (value > 0) {
        vatAmount = subTotal.div(100).times(value)
    }
    this.text = "£ $vatAmount"
    return vatAmount
}

fun EditText.enabled() {
    this.isEnabled = true
}


infix fun <T : String> TextView.round(value: T) {
    this.text = value.toBigDecimal().setScale(2, RoundingMode.UP).toString()
}

infix fun TextView.round(value: Float) {
    var value = value.toDouble()
    this.text = "" + value.toBigDecimal().setScale(2, RoundingMode.UP).toString()
}


infix fun Boolean.selected(service: Boolean): String {
    return if (this && service) {
        "both"
    } else if (this && !service) {
        "garage"
    } else
        "mobile service"
}

infix fun String.canCall(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$this")
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Application Not Found", Toast.LENGTH_SHORT).show()
    }
}

infix fun String.canText(context: Context) {
    try {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", this)
        context.startActivity(smsIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Application Not Found", Toast.LENGTH_SHORT).show()
    }
}

infix fun String.canEmail(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(this))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Garage App")
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Application Not Found", Toast.LENGTH_SHORT).show()
    }
}

fun TextView.string(): String {
    if (this.text.toString().isNullOrEmpty()) {
        return ""
    } else {
        return this.text.toString()
    }
}
