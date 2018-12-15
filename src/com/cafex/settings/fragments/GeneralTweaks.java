/*
 * Copyright © 2018-2019 Syberia Project
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

package com.cafex.settings.fragments;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import com.android.settings.R;

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.cafex.settings.preference.CustomSeekBarPreference;
import com.cafex.settings.preference.SecureSettingSwitchPreference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.Indexable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class GeneralTweaks extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String RINGTONE_FOCUS_MODE = "ringtone_focus_mode";

    private ListPreference mHeadsetRingtoneFocus;

    private static final String SYSUI_ROUNDED_SIZE = "sysui_rounded_size";
    private static final String SYSUI_ROUNDED_CONTENT_PADDING = "sysui_rounded_content_padding";
    private static final String SYSUI_ROUNDED_FWVALS = "sysui_rounded_fwvals";

    private CustomSeekBarPreference mCornerRadius;
    private CustomSeekBarPreference mContentPadding;
    private SecureSettingSwitchPreference mRoundedFwvals;
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.general_tweaks);
         final ContentResolver resolver = getActivity().getContentResolver();

        mHeadsetRingtoneFocus = (ListPreference) findPreference(RINGTONE_FOCUS_MODE);
        int mHeadsetRingtoneFocusValue = Settings.Global.getInt(resolver,
                Settings.Global.RINGTONE_FOCUS_MODE, 0);
        mHeadsetRingtoneFocus.setValue(Integer.toString(mHeadsetRingtoneFocusValue));
        mHeadsetRingtoneFocus.setSummary(mHeadsetRingtoneFocus.getEntry());
        mHeadsetRingtoneFocus.setOnPreferenceChangeListener(this);       
        Resources res = null;
        Context ctx = getContext();
        float density = Resources.getSystem().getDisplayMetrics().density;

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        // Rounded Corner Radius
        mCornerRadius = (CustomSeekBarPreference) findPreference(SYSUI_ROUNDED_SIZE);
        int resourceIdRadius = (int) ctx.getResources().getDimension(com.android.internal.R.dimen.rounded_corner_radius);
        int cornerRadius = Settings.Secure.getIntForUser(ctx.getContentResolver(), Settings.Secure.SYSUI_ROUNDED_SIZE,
                ((int) (resourceIdRadius / density)), UserHandle.USER_CURRENT);
        mCornerRadius.setValue(cornerRadius);
        mCornerRadius.setOnPreferenceChangeListener(this);

        // Rounded Content Padding
        //mContentPadding = (CustomSeekBarPreference) findPreference(SYSUI_ROUNDED_CONTENT_PADDING);
        //int resourceIdPadding = res.getIdentifier("com.android.systemui:dimen/rounded_corner_content_padding", null,
        //        null);
        //int contentPadding = Settings.Secure.getIntForUser(ctx.getContentResolver(),
        //        Settings.Secure.SYSUI_ROUNDED_CONTENT_PADDING,
        //        (int) (res.getDimension(resourceIdPadding) / density), UserHandle.USER_CURRENT);
        //mContentPadding.setValue(contentPadding);
        //mContentPadding.setOnPreferenceChangeListener(this);

        // Rounded use Framework Values
        mRoundedFwvals = (SecureSettingSwitchPreference) findPreference(SYSUI_ROUNDED_FWVALS);
        mRoundedFwvals.setOnPreferenceChangeListener(this);       
        
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHeadsetRingtoneFocus) {
            int mHeadsetRingtoneFocusValue = Integer.valueOf((String) newValue);
            int index = mHeadsetRingtoneFocus.findIndexOfValue((String) newValue);
            mHeadsetRingtoneFocus.setSummary(
                    mHeadsetRingtoneFocus.getEntries()[index]);
            Settings.Global.putInt(getContentResolver(), Settings.Global.RINGTONE_FOCUS_MODE,
                    mHeadsetRingtoneFocusValue);
            return true;
        } else if (preference == mCornerRadius) {
            Settings.Secure.putIntForUser(getContext().getContentResolver(), Settings.Secure.SYSUI_ROUNDED_SIZE,
                    (int) objValue, UserHandle.USER_CURRENT);
            return true;
        //} else if (preference == mContentPadding) {
        //    Settings.Secure.putIntForUser(getContext().getContentResolver(), Settings.Secure.SYSUI_ROUNDED_CONTENT_PADDING,
        //            (int) objValue, UserHandle.USER_CURRENT);
        //    return true;
        } else if (preference == mRoundedFwvals) {
            restoreCorners();
            return true;            
        }
        return false;
    }

    private void restoreCorners() {
        Resources res = null;
        float density = Resources.getSystem().getDisplayMetrics().density;
        Context ctx = getContext();

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        int resourceIdRadius = (int) ctx.getResources().getDimension(com.android.internal.R.dimen.rounded_corner_radius);
        //int resourceIdPadding = res.getIdentifier("com.android.systemui:dimen/rounded_corner_content_padding", null, null);
        mCornerRadius.setValue((int) (resourceIdRadius / density));
        //mContentPadding.setValue((int) (res.getDimension(resourceIdPadding) / density));
    }
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CAFEX;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.general_tweaks;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}