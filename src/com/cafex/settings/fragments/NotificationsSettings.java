/*
 * Copyright © 2018-2020 Syberia Project
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
import android.content.Context;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import android.text.TextUtils;
import android.content.res.Resources;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import android.provider.Settings;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.android.settingslib.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import com.cafex.settings.preference.CustomSeekBarPreference;
import com.cafex.settings.preference.SystemSettingListPreference;
import com.cafex.settings.preference.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SearchIndexable
public class NotificationsSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.notifications_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
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
                    sir.xmlResId = R.xml.notifications_settings;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };

}
