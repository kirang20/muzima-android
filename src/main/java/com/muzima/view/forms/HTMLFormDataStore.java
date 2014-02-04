package com.muzima.view.forms;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.muzima.api.model.FormData;
import com.muzima.controller.FormController;

public class HTMLFormDataStore {
    private static final String TAG = "FormDataStore";

    private HTMLFormWebViewActivity formWebViewActivity;
    private FormController formController;
    private FormData formData;

    public HTMLFormDataStore(HTMLFormWebViewActivity formWebViewActivity, FormController formController, FormData formData) {
        this.formWebViewActivity = formWebViewActivity;
        this.formController = formController;
        this.formData = formData;
    }

    @JavascriptInterface
    public void saveHTML(String jsonPayload, String status) {
        formData.setJsonPayload(jsonPayload);
        formData.setStatus(status);
        try {
            formController.saveFormData(formData);
            formWebViewActivity.setResult(FormsActivity.RESULT_OK);
            formWebViewActivity.finish();
        } catch (FormController.FormDataSaveException e) {
            Toast.makeText(formWebViewActivity, "An error occurred while saving the form", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exception occurred while saving form data" + e);
        }
    }
}