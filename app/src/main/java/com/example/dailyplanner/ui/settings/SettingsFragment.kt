package com.example.dailyplanner.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dailyplanner.R
import com.example.dailyplanner.database.*
import com.example.dailyplanner.databinding.FragmentSettingsBinding
import com.example.dailyplanner.utilities.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE

        initFunc()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFunc() {
        APP_ACTIVITY.binding.fab.setOnClickListener(null)
        deleteAllScheduleNotes()
        binding.nameProfile.text = USER.name
        Picasso.get().load(USER.image_profile_url).placeholder(R.drawable.ic_profile_default).into(binding.imageEdit)
        binding.logOutAccount.setOnClickListener()
        {
            AUTH.signOut()
            restartActivity()
        }
        binding.imageEdit.setOnClickListener {
            editImage()
        }
    }

    private fun deleteAllScheduleNotes() {
        binding.deleteAllScheduleBlock.setOnClickListener {
            val textTitle = TextView(APP_ACTIVITY)
            textTitle.text = "Вы действительно хотите удалить всё расписание?"
            textTitle.gravity = Gravity.CENTER
            textTitle.textSize = 18f
            textTitle.setPadding(20, 20, 20, 30)
            textTitle.setTextColor(APP_ACTIVITY.getColor(R.color.white))
            val builder = AlertDialog.Builder(
                ContextThemeWrapper(APP_ACTIVITY, R.style.AlertDialogCustom)
            )
            builder.setCustomTitle(textTitle)
            builder.setPositiveButton("Да") { _, _ ->
                REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).removeValue()
                REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID)
                    .removeValue().addOnSuccessListener {
                        showToast("Всё расписание успешно удалено")
                    }
            }
            builder.setNegativeButton("Нет") { _, _ -> }
            builder.show()
        }

        binding.deleteAllNotesBlock.setOnClickListener {
            val textTitle = TextView(APP_ACTIVITY)
            textTitle.text = "Вы действительно хотите удалить все заметки?"
            textTitle.gravity = Gravity.CENTER
            textTitle.textSize = 18f
            textTitle.setPadding(20, 20, 20, 30)
            textTitle.setTextColor(APP_ACTIVITY.getColor(R.color.white))
            val builder = AlertDialog.Builder(
                ContextThemeWrapper(APP_ACTIVITY, R.style.AlertDialogCustom)
            )
            builder.setCustomTitle(textTitle)
            builder.setPositiveButton("Да") { _, _ ->
                REF_DATABASE_ROOT.child(NODE_NOTES).child(UID)
                    .removeValue().addOnSuccessListener {
                        showToast("Все заметки успешно удалены")
                    }
            }
            builder.setNegativeButton("Нет") { _, _ -> }
            builder.show()
        }
    }

    private fun editImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 301)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val uri = data.data
            uploadFileToStorage(uri!!)
        }
    }

    private fun uploadFileToStorage(uri: Uri) {
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(UID)
        putFileToStorage(uri, path) {
            getUrlFromStorage(path) { path ->
                REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_IMAGE_PROFILE_URL)
                    .setValue(path).addOnSuccessListener { USER.image_profile_url = path }
                Picasso.get().load(path).into(binding.imageEdit)
            }
        }
    }

    private inline fun getUrlFromStorage(
        path: StorageReference,
        crossinline function: (url: String) -> Unit
    ) {
        path.downloadUrl.addOnSuccessListener { function(it.toString()) }.addOnFailureListener {
            showToast(it.message.toString()) }
    }

    private inline fun putFileToStorage(
        uri: Uri,
        path: StorageReference,
        crossinline function: () -> Unit
    ) {
        path.putFile(uri).addOnSuccessListener { function() }.addOnFailureListener {
            showToast(it.message.toString()) }
    }
}