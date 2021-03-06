/*
 * Copyright (c) 2014. The Trustees of Indiana University.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license with additional
 * healthcare disclaimer. If the user is an entity intending to commercialize any application
 * that uses this code in a for-profit venture, please contact the copyright holder.
 */

/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.muzima.adapters.forms;

import android.content.Context;
import android.util.Log;
import com.muzima.R;
import com.muzima.controller.FormController;
import com.muzima.model.CompleteForm;
import com.muzima.model.CompleteFormWithPatientData;
import com.muzima.model.collections.CompleteForms;
import com.muzima.tasks.FormsAdapterBackgroundQueryTask;

/**
 * Responsible to display all the completed forms for a specific patient.
 */
public class PatientCompleteFormsAdapter extends FormsWithDataAdapter<CompleteFormWithPatientData> {
    private static final String TAG = "PatientCompleteFormsAdapter";
    private String patientId;

    public PatientCompleteFormsAdapter(Context context, int textViewResourceId, FormController formController, String patientId) {
        super(context, textViewResourceId, formController);
        this.patientId = patientId;
    }

    @Override
    public void reloadData() {
        new BackgroundQueryTask(this).execute();
    }

    public String getPatientId() {
        return patientId;
    }

    @Override
    protected int getFormItemLayout() {
        return R.layout.item_forms_list_selectable;
    }

    /**
     * Responsible to fetch all the incomplete forms for a specific patient.
     */
    public static class BackgroundQueryTask extends FormsAdapterBackgroundQueryTask<CompleteForm> {

        public BackgroundQueryTask(FormsAdapter formsAdapter) {
            super(formsAdapter);
        }

        @Override
        protected CompleteForms doInBackground(Void... voids) {
            CompleteForms completePatientForms = null;

            if (adapterWeakReference.get() != null) {
                try {
                    FormsAdapter formsAdapter = adapterWeakReference.get();
                    completePatientForms = formsAdapter.getFormController()
                            .getAllCompleteFormsForPatientUuid(((PatientCompleteFormsAdapter) formsAdapter).getPatientId());

                    Log.i(TAG, "#Complete forms: " + completePatientForms.size());
                } catch (FormController.FormFetchException e) {
                    Log.w(TAG, "Exception occurred while fetching local forms ", e);
                }
            }

            return completePatientForms;
        }
    }
}
