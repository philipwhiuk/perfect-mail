package com.whiuk.philip.perfect.activity.setup;


import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.widget.Toast;

import com.whiuk.philip.perfect.PerfectMail;
import com.whiuk.philip.perfect.PerfectMail.NotificationHideSubject;
import com.whiuk.philip.perfect.PerfectMail.NotificationQuickDelete;
import com.whiuk.philip.perfect.PerfectMail.SplitViewMode;
import com.whiuk.philip.perfect.Preferences;
import com.whiuk.philip.perfect.R;
import com.whiuk.philip.perfect.activity.ColorPickerDialog;
import com.whiuk.philip.perfect.activity.MailPreferenceActivity;
import com.whiuk.philip.perfect.helper.FileBrowserHelper;
import com.whiuk.philip.perfect.helper.FileBrowserHelper.FileBrowserFailOverCallback;
import com.whiuk.philip.perfect.notification.NotificationController;
import com.whiuk.philip.perfect.preferences.CheckBoxListPreference;
import com.whiuk.philip.perfect.preferences.Storage;
import com.whiuk.philip.perfect.preferences.StorageEditor;
import com.whiuk.philip.perfect.preferences.TimePickerPreference;
import com.whiuk.philip.perfect.service.MailService;
import com.whiuk.philip.perfect.ui.dialog.ApgDeprecationWarningDialog;
import org.openintents.openpgp.util.OpenPgpAppPreference;


public class Prefs extends MailPreferenceActivity {

    /**
     * Immutable empty {@link CharSequence} array
     */
    private static final CharSequence[] EMPTY_CHAR_SEQUENCE_ARRAY = new CharSequence[0];

    /*
     * Keys of the preferences defined in res/xml/global_preferences.xml
     */
    private static final String PREFERENCE_THEME = "theme";
    private static final String PREFERENCE_MESSAGE_VIEW_THEME = "messageViewTheme";
    private static final String PREFERENCE_FIXED_MESSAGE_THEME = "fixed_message_view_theme";
    private static final String PREFERENCE_COMPOSER_THEME = "message_compose_theme";
    private static final String PREFERENCE_FONT_SIZE = "font_size";
    private static final String PREFERENCE_ANIMATIONS = "animations";
    private static final String PREFERENCE_GESTURES = "gestures";
    private static final String PREFERENCE_VOLUME_NAVIGATION = "volume_navigation";
    private static final String PREFERENCE_START_INTEGRATED_INBOX = "start_integrated_inbox";
    private static final String PREFERENCE_CONFIRM_ACTIONS = "confirm_actions";
    private static final String PREFERENCE_NOTIFICATION_HIDE_SUBJECT = "notification_hide_subject";
    private static final String PREFERENCE_MEASURE_ACCOUNTS = "measure_accounts";
    private static final String PREFERENCE_COUNT_SEARCH = "count_search";
    private static final String PREFERENCE_HIDE_SPECIAL_ACCOUNTS = "hide_special_accounts";
    private static final String PREFERENCE_MESSAGELIST_CHECKBOXES = "messagelist_checkboxes";
    private static final String PREFERENCE_MESSAGELIST_PREVIEW_LINES = "messagelist_preview_lines";
    private static final String PREFERENCE_MESSAGELIST_SENDER_ABOVE_SUBJECT = "messagelist_sender_above_subject";
    private static final String PREFERENCE_MESSAGELIST_STARS = "messagelist_stars";
    private static final String PREFERENCE_MESSAGELIST_SHOW_CORRESPONDENT_NAMES = "messagelist_show_correspondent_names";
    private static final String PREFERENCE_MESSAGELIST_SHOW_CONTACT_NAME = "messagelist_show_contact_name";
    private static final String PREFERENCE_MESSAGELIST_CONTACT_NAME_COLOR = "messagelist_contact_name_color";
    private static final String PREFERENCE_MESSAGELIST_SHOW_CONTACT_PICTURE = "messagelist_show_contact_picture";
    private static final String PREFERENCE_MESSAGELIST_COLORIZE_MISSING_CONTACT_PICTURES =
            "messagelist_colorize_missing_contact_pictures";
    private static final String PREFERENCE_MESSAGEVIEW_FIXEDWIDTH = "messageview_fixedwidth_font";
    private static final String PREFERENCE_MESSAGEVIEW_VISIBLE_REFILE_ACTIONS = "messageview_visible_refile_actions";

    private static final String PREFERENCE_MESSAGEVIEW_RETURN_TO_LIST = "messageview_return_to_list";
    private static final String PREFERENCE_MESSAGEVIEW_SHOW_NEXT = "messageview_show_next";
    private static final String PREFERENCE_QUIET_TIME_ENABLED = "quiet_time_enabled";
    private static final String PREFERENCE_DISABLE_NOTIFICATION_DURING_QUIET_TIME =
            "disable_notifications_during_quiet_time";
    private static final String PREFERENCE_QUIET_TIME_STARTS = "quiet_time_starts";
    private static final String PREFERENCE_QUIET_TIME_ENDS = "quiet_time_ends";
    private static final String PREFERENCE_NOTIF_QUICK_DELETE = "notification_quick_delete";
    private static final String PREFERENCE_LOCK_SCREEN_NOTIFICATION_VISIBILITY = "lock_screen_notification_visibility";
    private static final String PREFERENCE_HIDE_USERAGENT = "privacy_hide_useragent";
    private static final String PREFERENCE_HIDE_TIMEZONE = "privacy_hide_timezone";

    private static final String PREFERENCE_OPENPGP_PROVIDER = "openpgp_provider";
    private static final String PREFERENCE_OPENPGP_SUPPORT_SIGN_ONLY = "openpgp_support_sign_only";

    private static final String PREFERENCE_AUTOFIT_WIDTH = "messageview_autofit_width";
    private static final String PREFERENCE_BACKGROUND_OPS = "background_ops";
    private static final String PREFERENCE_DEBUG_LOGGING = "debug_logging";
    private static final String PREFERENCE_SENSITIVE_LOGGING = "sensitive_logging";

    private static final String PREFERENCE_ATTACHMENT_DEF_PATH = "attachment_default_path";
    private static final String PREFERENCE_BACKGROUND_AS_UNREAD_INDICATOR = "messagelist_background_as_unread_indicator";
    private static final String PREFERENCE_THREADED_VIEW = "threaded_view";
    private static final String PREFERENCE_FOLDERLIST_WRAP_NAME = "folderlist_wrap_folder_name";
    private static final String PREFERENCE_SPLITVIEW_MODE = "splitview_mode";

    private static final String APG_PROVIDER_PLACEHOLDER = "apg-placeholder";

    private static final int ACTIVITY_CHOOSE_FOLDER = 1;

    private static final int DIALOG_APG_DEPRECATION_WARNING = 1;

    // Named indices for the mVisibleRefileActions field
    private static final int VISIBLE_REFILE_ACTIONS_DELETE = 0;
    private static final int VISIBLE_REFILE_ACTIONS_ARCHIVE = 1;
    private static final int VISIBLE_REFILE_ACTIONS_MOVE = 2;
    private static final int VISIBLE_REFILE_ACTIONS_COPY = 3;
    private static final int VISIBLE_REFILE_ACTIONS_SPAM = 4;

    private ListPreference mTheme;
    private CheckBoxPreference mFixedMessageTheme;
    private ListPreference mMessageTheme;
    private ListPreference mComposerTheme;
    private CheckBoxPreference mAnimations;
    private CheckBoxPreference mGestures;
    private CheckBoxListPreference mVolumeNavigation;
    private CheckBoxPreference mStartIntegratedInbox;
    private CheckBoxListPreference mConfirmActions;
    private ListPreference mNotificationHideSubject;
    private CheckBoxPreference mMeasureAccounts;
    private CheckBoxPreference mCountSearch;
    private CheckBoxPreference mHideSpecialAccounts;
    private ListPreference mPreviewLines;
    private CheckBoxPreference mSenderAboveSubject;
    private CheckBoxPreference mCheckboxes;
    private CheckBoxPreference mStars;
    private CheckBoxPreference mShowCorrespondentNames;
    private CheckBoxPreference mShowContactName;
    private CheckBoxPreference mChangeContactNameColor;
    private CheckBoxPreference mShowContactPicture;
    private CheckBoxPreference mColorizeMissingContactPictures;
    private CheckBoxPreference mFixedWidth;
    private CheckBoxPreference mReturnToList;
    private CheckBoxPreference mShowNext;
    private CheckBoxPreference mAutofitWidth;
    private ListPreference mBackgroundOps;
    private CheckBoxPreference mDebugLogging;
    private CheckBoxPreference mSensitiveLogging;
    private CheckBoxPreference mHideUserAgent;
    private CheckBoxPreference mHideTimeZone;
    private CheckBoxPreference mWrapFolderNames;
    private CheckBoxListPreference mVisibleRefileActions;

    private OpenPgpAppPreference mOpenPgpProvider;
    private CheckBoxPreference mOpenPgpSupportSignOnly;

    private CheckBoxPreference mQuietTimeEnabled;
    private CheckBoxPreference mDisableNotificationDuringQuietTime;
    private com.whiuk.philip.perfect.preferences.TimePickerPreference mQuietTimeStarts;
    private com.whiuk.philip.perfect.preferences.TimePickerPreference mQuietTimeEnds;
    private ListPreference mNotificationQuickDelete;
    private ListPreference mLockScreenNotificationVisibility;
    private Preference mAttachmentPathPreference;

    private CheckBoxPreference mBackgroundAsUnreadIndicator;
    private CheckBoxPreference mThreadedView;
    private ListPreference mSplitViewMode;


    public static void actionPrefs(Context context) {
        Intent i = new Intent(context, Prefs.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.global_preferences);

        mTheme = setupListPreference(PREFERENCE_THEME, themeIdToName(PerfectMail.getMailTheme()));
        mFixedMessageTheme = (CheckBoxPreference) findPreference(PREFERENCE_FIXED_MESSAGE_THEME);
        mFixedMessageTheme.setChecked(PerfectMail.useFixedMessageViewTheme());
        mMessageTheme = setupListPreference(PREFERENCE_MESSAGE_VIEW_THEME,
                themeIdToName(PerfectMail.getMailMessageViewThemeSetting()));
        mComposerTheme = setupListPreference(PREFERENCE_COMPOSER_THEME,
                themeIdToName(PerfectMail.getMailComposerThemeSetting()));

        findPreference(PREFERENCE_FONT_SIZE).setOnPreferenceClickListener(
        new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                onFontSizeSettings();
                return true;
            }
        });

        mAnimations = (CheckBoxPreference)findPreference(PREFERENCE_ANIMATIONS);
        mAnimations.setChecked(PerfectMail.showAnimations());

        mGestures = (CheckBoxPreference)findPreference(PREFERENCE_GESTURES);
        mGestures.setChecked(PerfectMail.gesturesEnabled());

        mVolumeNavigation = (CheckBoxListPreference)findPreference(PREFERENCE_VOLUME_NAVIGATION);
        mVolumeNavigation.setItems(new CharSequence[] {getString(R.string.volume_navigation_message), getString(R.string.volume_navigation_list)});
        mVolumeNavigation.setCheckedItems(new boolean[] { PerfectMail.useVolumeKeysForNavigationEnabled(), PerfectMail.useVolumeKeysForListNavigationEnabled()});

        mStartIntegratedInbox = (CheckBoxPreference)findPreference(PREFERENCE_START_INTEGRATED_INBOX);
        mStartIntegratedInbox.setChecked(PerfectMail.startIntegratedInbox());

        mConfirmActions = (CheckBoxListPreference) findPreference(PREFERENCE_CONFIRM_ACTIONS);

        boolean canDeleteFromNotification = NotificationController.platformSupportsExtendedNotifications();
        CharSequence[] confirmActionEntries = new CharSequence[canDeleteFromNotification ? 6 : 5];
        boolean[] confirmActionValues = new boolean[confirmActionEntries.length];
        int index = 0;

        confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete);
        confirmActionValues[index++] = PerfectMail.confirmDelete();
        confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete_starred);
        confirmActionValues[index++] = PerfectMail.confirmDeleteStarred();
        if (canDeleteFromNotification) {
            confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_delete_notif);
            confirmActionValues[index++] = PerfectMail.confirmDeleteFromNotification();
        }
        confirmActionEntries[index] = getString(R.string.global_settings_confirm_action_spam);
        confirmActionValues[index++] = PerfectMail.confirmSpam();
        confirmActionEntries[index] = getString(R.string.global_settings_confirm_menu_discard);
        confirmActionValues[index++] = PerfectMail.confirmDiscardMessage();
        confirmActionEntries[index] = getString(R.string.global_settings_confirm_menu_mark_all_read);
        confirmActionValues[index++] = PerfectMail.confirmMarkAllRead();

        mConfirmActions.setItems(confirmActionEntries);
        mConfirmActions.setCheckedItems(confirmActionValues);

        mNotificationHideSubject = setupListPreference(PREFERENCE_NOTIFICATION_HIDE_SUBJECT,
                PerfectMail.getNotificationHideSubject().toString());

        mMeasureAccounts = (CheckBoxPreference)findPreference(PREFERENCE_MEASURE_ACCOUNTS);
        mMeasureAccounts.setChecked(PerfectMail.measureAccounts());

        mCountSearch = (CheckBoxPreference)findPreference(PREFERENCE_COUNT_SEARCH);
        mCountSearch.setChecked(PerfectMail.countSearchMessages());

        mHideSpecialAccounts = (CheckBoxPreference)findPreference(PREFERENCE_HIDE_SPECIAL_ACCOUNTS);
        mHideSpecialAccounts.setChecked(PerfectMail.isHideSpecialAccounts());


        mPreviewLines = setupListPreference(PREFERENCE_MESSAGELIST_PREVIEW_LINES,
                                            Integer.toString(PerfectMail.messageListPreviewLines()));

        mSenderAboveSubject = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_SENDER_ABOVE_SUBJECT);
        mSenderAboveSubject.setChecked(PerfectMail.messageListSenderAboveSubject());
        mCheckboxes = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_CHECKBOXES);
        mCheckboxes.setChecked(PerfectMail.messageListCheckboxes());

        mStars = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_STARS);
        mStars.setChecked(PerfectMail.messageListStars());

        mShowCorrespondentNames = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_SHOW_CORRESPONDENT_NAMES);
        mShowCorrespondentNames.setChecked(PerfectMail.showCorrespondentNames());

        mShowContactName = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_SHOW_CONTACT_NAME);
        mShowContactName.setChecked(PerfectMail.showContactName());

        mShowContactPicture = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_SHOW_CONTACT_PICTURE);
        mShowContactPicture.setChecked(PerfectMail.showContactPicture());

        mColorizeMissingContactPictures = (CheckBoxPreference)findPreference(
                PREFERENCE_MESSAGELIST_COLORIZE_MISSING_CONTACT_PICTURES);
        mColorizeMissingContactPictures.setChecked(PerfectMail.isColorizeMissingContactPictures());

        mBackgroundAsUnreadIndicator = (CheckBoxPreference)findPreference(PREFERENCE_BACKGROUND_AS_UNREAD_INDICATOR);
        mBackgroundAsUnreadIndicator.setChecked(PerfectMail.useBackgroundAsUnreadIndicator());

        mChangeContactNameColor = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGELIST_CONTACT_NAME_COLOR);
        mChangeContactNameColor.setChecked(PerfectMail.changeContactNameColor());

        mThreadedView = (CheckBoxPreference) findPreference(PREFERENCE_THREADED_VIEW);
        mThreadedView.setChecked(PerfectMail.isThreadedViewEnabled());

        if (PerfectMail.changeContactNameColor()) {
            mChangeContactNameColor.setSummary(R.string.global_settings_registered_name_color_changed);
        } else {
            mChangeContactNameColor.setSummary(R.string.global_settings_registered_name_color_default);
        }
        mChangeContactNameColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final Boolean checked = (Boolean) newValue;
                if (checked) {
                    onChooseContactNameColor();
                    mChangeContactNameColor.setSummary(R.string.global_settings_registered_name_color_changed);
                } else {
                    mChangeContactNameColor.setSummary(R.string.global_settings_registered_name_color_default);
                }
                mChangeContactNameColor.setChecked(checked);
                return false;
            }
        });

        mFixedWidth = (CheckBoxPreference)findPreference(PREFERENCE_MESSAGEVIEW_FIXEDWIDTH);
        mFixedWidth.setChecked(PerfectMail.messageViewFixedWidthFont());

        mReturnToList = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_RETURN_TO_LIST);
        mReturnToList.setChecked(PerfectMail.messageViewReturnToList());

        mShowNext = (CheckBoxPreference) findPreference(PREFERENCE_MESSAGEVIEW_SHOW_NEXT);
        mShowNext.setChecked(PerfectMail.messageViewShowNext());

        mAutofitWidth = (CheckBoxPreference) findPreference(PREFERENCE_AUTOFIT_WIDTH);
        mAutofitWidth.setChecked(PerfectMail.autofitWidth());

        mQuietTimeEnabled = (CheckBoxPreference) findPreference(PREFERENCE_QUIET_TIME_ENABLED);
        mQuietTimeEnabled.setChecked(PerfectMail.getQuietTimeEnabled());

        mDisableNotificationDuringQuietTime = (CheckBoxPreference) findPreference(
                PREFERENCE_DISABLE_NOTIFICATION_DURING_QUIET_TIME);
        mDisableNotificationDuringQuietTime.setChecked(!PerfectMail.isNotificationDuringQuietTimeEnabled());
        mQuietTimeStarts = (TimePickerPreference) findPreference(PREFERENCE_QUIET_TIME_STARTS);
        mQuietTimeStarts.setDefaultValue(PerfectMail.getQuietTimeStarts());
        mQuietTimeStarts.setSummary(PerfectMail.getQuietTimeStarts());
        mQuietTimeStarts.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final String time = (String) newValue;
                mQuietTimeStarts.setSummary(time);
                return false;
            }
        });

        mQuietTimeEnds = (TimePickerPreference) findPreference(PREFERENCE_QUIET_TIME_ENDS);
        mQuietTimeEnds.setSummary(PerfectMail.getQuietTimeEnds());
        mQuietTimeEnds.setDefaultValue(PerfectMail.getQuietTimeEnds());
        mQuietTimeEnds.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final String time = (String) newValue;
                mQuietTimeEnds.setSummary(time);
                return false;
            }
        });

        mNotificationQuickDelete = setupListPreference(PREFERENCE_NOTIF_QUICK_DELETE,
                PerfectMail.getNotificationQuickDeleteBehaviour().toString());
        if (!NotificationController.platformSupportsExtendedNotifications()) {
            PreferenceScreen prefs = (PreferenceScreen) findPreference("notification_preferences");
            prefs.removePreference(mNotificationQuickDelete);
            mNotificationQuickDelete = null;
        }

        mLockScreenNotificationVisibility = setupListPreference(PREFERENCE_LOCK_SCREEN_NOTIFICATION_VISIBILITY,
            PerfectMail.getLockScreenNotificationVisibility().toString());
        if (!NotificationController.platformSupportsLockScreenNotifications()) {
            ((PreferenceScreen) findPreference("notification_preferences"))
                .removePreference(mLockScreenNotificationVisibility);
            mLockScreenNotificationVisibility = null;
        }

        mBackgroundOps = setupListPreference(PREFERENCE_BACKGROUND_OPS, PerfectMail.getBackgroundOps().name());

        mDebugLogging = (CheckBoxPreference)findPreference(PREFERENCE_DEBUG_LOGGING);
        mSensitiveLogging = (CheckBoxPreference)findPreference(PREFERENCE_SENSITIVE_LOGGING);
        mHideUserAgent = (CheckBoxPreference)findPreference(PREFERENCE_HIDE_USERAGENT);
        mHideTimeZone = (CheckBoxPreference)findPreference(PREFERENCE_HIDE_TIMEZONE);

        mDebugLogging.setChecked(PerfectMail.isDebug());
        mSensitiveLogging.setChecked(PerfectMail.DEBUG_SENSITIVE);
        mHideUserAgent.setChecked(PerfectMail.hideUserAgent());
        mHideTimeZone.setChecked(PerfectMail.hideTimeZone());

        mOpenPgpProvider = (OpenPgpAppPreference) findPreference(PREFERENCE_OPENPGP_PROVIDER);
        mOpenPgpProvider.setValue(PerfectMail.getOpenPgpProvider());
        if (OpenPgpAppPreference.isApgInstalled(getApplicationContext())) {
            mOpenPgpProvider.addLegacyProvider(
                    APG_PROVIDER_PLACEHOLDER, getString(R.string.apg), R.drawable.ic_apg_small);
        }
        mOpenPgpProvider.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                if (APG_PROVIDER_PLACEHOLDER.equals(value)) {
                    mOpenPgpProvider.setValue("");
                    showDialog(DIALOG_APG_DEPRECATION_WARNING);
                } else {
                    mOpenPgpProvider.setValue(value);
                }
                return false;
            }
        });

        mOpenPgpSupportSignOnly = (CheckBoxPreference) findPreference(PREFERENCE_OPENPGP_SUPPORT_SIGN_ONLY);
        mOpenPgpSupportSignOnly.setChecked(PerfectMail.getOpenPgpSupportSignOnly());

        mAttachmentPathPreference = findPreference(PREFERENCE_ATTACHMENT_DEF_PATH);
        mAttachmentPathPreference.setSummary(PerfectMail.getAttachmentDefaultPath());
        mAttachmentPathPreference
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FileBrowserHelper
                .getInstance()
                .showFileBrowserActivity(Prefs.this,
                                         new File(PerfectMail.getAttachmentDefaultPath()),
                                         ACTIVITY_CHOOSE_FOLDER, callback);

                return true;
            }

            FileBrowserFailOverCallback callback = new FileBrowserFailOverCallback() {

                @Override
                public void onPathEntered(String path) {
                    mAttachmentPathPreference.setSummary(path);
                    PerfectMail.setAttachmentDefaultPath(path);
                }

                @Override
                public void onCancel() {
                    // canceled, do nothing
                }
            };
        });

        mWrapFolderNames = (CheckBoxPreference)findPreference(PREFERENCE_FOLDERLIST_WRAP_NAME);
        mWrapFolderNames.setChecked(PerfectMail.wrapFolderNames());

        mVisibleRefileActions = (CheckBoxListPreference) findPreference(PREFERENCE_MESSAGEVIEW_VISIBLE_REFILE_ACTIONS);
        CharSequence[] visibleRefileActionsEntries = new CharSequence[5];
        visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_DELETE] = getString(R.string.delete_action);
        visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_ARCHIVE] = getString(R.string.archive_action);
        visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_MOVE] = getString(R.string.move_action);
        visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_COPY] = getString(R.string.copy_action);
        visibleRefileActionsEntries[VISIBLE_REFILE_ACTIONS_SPAM] = getString(R.string.spam_action);

        boolean[] visibleRefileActionsValues = new boolean[5];
        visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_DELETE] = PerfectMail.isMessageViewDeleteActionVisible();
        visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_ARCHIVE] = PerfectMail.isMessageViewArchiveActionVisible();
        visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_MOVE] = PerfectMail.isMessageViewMoveActionVisible();
        visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_COPY] = PerfectMail.isMessageViewCopyActionVisible();
        visibleRefileActionsValues[VISIBLE_REFILE_ACTIONS_SPAM] = PerfectMail.isMessageViewSpamActionVisible();

        mVisibleRefileActions.setItems(visibleRefileActionsEntries);
        mVisibleRefileActions.setCheckedItems(visibleRefileActionsValues);

        mSplitViewMode = (ListPreference) findPreference(PREFERENCE_SPLITVIEW_MODE);
        initListPreference(mSplitViewMode, PerfectMail.getSplitViewMode().name(),
                mSplitViewMode.getEntries(), mSplitViewMode.getEntryValues());
    }

    private static String themeIdToName(PerfectMail.Theme theme) {
        switch (theme) {
            case DARK: return "dark";
            case USE_GLOBAL: return "global";
            default: return "light";
        }
    }

    private static PerfectMail.Theme themeNameToId(String theme) {
        if (TextUtils.equals(theme, "dark")) {
            return PerfectMail.Theme.DARK;
        } else if (TextUtils.equals(theme, "global")) {
            return PerfectMail.Theme.USE_GLOBAL;
        } else {
            return PerfectMail.Theme.LIGHT;
        }
    }

    private void saveSettings() {
        Storage storage = Preferences.getPreferences(this).getStorage();

        PerfectMail.setK9Theme(themeNameToId(mTheme.getValue()));
        PerfectMail.setUseFixedMessageViewTheme(mFixedMessageTheme.isChecked());
        PerfectMail.setK9MessageViewThemeSetting(themeNameToId(mMessageTheme.getValue()));
        PerfectMail.setK9ComposerThemeSetting(themeNameToId(mComposerTheme.getValue()));

        PerfectMail.setAnimations(mAnimations.isChecked());
        PerfectMail.setGesturesEnabled(mGestures.isChecked());
        PerfectMail.setUseVolumeKeysForNavigation(mVolumeNavigation.getCheckedItems()[0]);
        PerfectMail.setUseVolumeKeysForListNavigation(mVolumeNavigation.getCheckedItems()[1]);
        PerfectMail.setStartIntegratedInbox(!mHideSpecialAccounts.isChecked() && mStartIntegratedInbox.isChecked());
        PerfectMail.setNotificationHideSubject(NotificationHideSubject.valueOf(mNotificationHideSubject.getValue()));

        int index = 0;
        PerfectMail.setConfirmDelete(mConfirmActions.getCheckedItems()[index++]);
        PerfectMail.setConfirmDeleteStarred(mConfirmActions.getCheckedItems()[index++]);
        if (NotificationController.platformSupportsExtendedNotifications()) {
            PerfectMail.setConfirmDeleteFromNotification(mConfirmActions.getCheckedItems()[index++]);
        }
        PerfectMail.setConfirmSpam(mConfirmActions.getCheckedItems()[index++]);
        PerfectMail.setConfirmDiscardMessage(mConfirmActions.getCheckedItems()[index++]);
        PerfectMail.setConfirmMarkAllRead(mConfirmActions.getCheckedItems()[index++]);

        PerfectMail.setMeasureAccounts(mMeasureAccounts.isChecked());
        PerfectMail.setCountSearchMessages(mCountSearch.isChecked());
        PerfectMail.setHideSpecialAccounts(mHideSpecialAccounts.isChecked());
        PerfectMail.setMessageListPreviewLines(Integer.parseInt(mPreviewLines.getValue()));
        PerfectMail.setMessageListCheckboxes(mCheckboxes.isChecked());
        PerfectMail.setMessageListStars(mStars.isChecked());
        PerfectMail.setShowCorrespondentNames(mShowCorrespondentNames.isChecked());
        PerfectMail.setMessageListSenderAboveSubject(mSenderAboveSubject.isChecked());
        PerfectMail.setShowContactName(mShowContactName.isChecked());
        PerfectMail.setShowContactPicture(mShowContactPicture.isChecked());
        PerfectMail.setColorizeMissingContactPictures(mColorizeMissingContactPictures.isChecked());
        PerfectMail.setUseBackgroundAsUnreadIndicator(mBackgroundAsUnreadIndicator.isChecked());
        PerfectMail.setThreadedViewEnabled(mThreadedView.isChecked());
        PerfectMail.setChangeContactNameColor(mChangeContactNameColor.isChecked());
        PerfectMail.setMessageViewFixedWidthFont(mFixedWidth.isChecked());
        PerfectMail.setMessageViewReturnToList(mReturnToList.isChecked());
        PerfectMail.setMessageViewShowNext(mShowNext.isChecked());
        PerfectMail.setAutofitWidth(mAutofitWidth.isChecked());
        PerfectMail.setQuietTimeEnabled(mQuietTimeEnabled.isChecked());

        boolean[] enabledRefileActions = mVisibleRefileActions.getCheckedItems();
        PerfectMail.setMessageViewDeleteActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_DELETE]);
        PerfectMail.setMessageViewArchiveActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_ARCHIVE]);
        PerfectMail.setMessageViewMoveActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_MOVE]);
        PerfectMail.setMessageViewCopyActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_COPY]);
        PerfectMail.setMessageViewSpamActionVisible(enabledRefileActions[VISIBLE_REFILE_ACTIONS_SPAM]);

        PerfectMail.setNotificationDuringQuietTimeEnabled(!mDisableNotificationDuringQuietTime.isChecked());
        PerfectMail.setQuietTimeStarts(mQuietTimeStarts.getTime());
        PerfectMail.setQuietTimeEnds(mQuietTimeEnds.getTime());
        PerfectMail.setWrapFolderNames(mWrapFolderNames.isChecked());

        if (mNotificationQuickDelete != null) {
            PerfectMail.setNotificationQuickDeleteBehaviour(
                    NotificationQuickDelete.valueOf(mNotificationQuickDelete.getValue()));
        }

        if(mLockScreenNotificationVisibility != null) {
            PerfectMail.setLockScreenNotificationVisibility(
                PerfectMail.LockScreenNotificationVisibility.valueOf(mLockScreenNotificationVisibility.getValue()));
        }

        PerfectMail.setSplitViewMode(SplitViewMode.valueOf(mSplitViewMode.getValue()));
        PerfectMail.setAttachmentDefaultPath(mAttachmentPathPreference.getSummary().toString());
        boolean needsRefresh = PerfectMail.setBackgroundOps(mBackgroundOps.getValue());

        if (!PerfectMail.isDebug() && mDebugLogging.isChecked()) {
            Toast.makeText(this, R.string.debug_logging_enabled, Toast.LENGTH_LONG).show();
        }
        PerfectMail.setDebug(mDebugLogging.isChecked());
        PerfectMail.DEBUG_SENSITIVE = mSensitiveLogging.isChecked();
        PerfectMail.setHideUserAgent(mHideUserAgent.isChecked());
        PerfectMail.setHideTimeZone(mHideTimeZone.isChecked());

        PerfectMail.setOpenPgpProvider(mOpenPgpProvider.getValue());
        PerfectMail.setOpenPgpSupportSignOnly(mOpenPgpSupportSignOnly.isChecked());

        StorageEditor editor = storage.edit();
        PerfectMail.save(editor);
        editor.commit();

        if (needsRefresh) {
            MailService.actionReset(this, null);
        }
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    private void onFontSizeSettings() {
        FontSizeSettings.actionEditSettings(this);
    }

    private void onChooseContactNameColor() {
        new ColorPickerDialog(this, new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                PerfectMail.setContactNameColor(color);
            }
        },
        PerfectMail.getContactNameColor()).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_APG_DEPRECATION_WARNING: {
                dialog = new ApgDeprecationWarningDialog(this);
                dialog.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mOpenPgpProvider.show();
                    }
                });
                break;
            }

        }
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case ACTIVITY_CHOOSE_FOLDER:
            if (resultCode == RESULT_OK && data != null) {
                // obtain the filename
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String filePath = fileUri.getPath();
                    if (filePath != null) {
                        mAttachmentPathPreference.setSummary(filePath.toString());
                        PerfectMail.setAttachmentDefaultPath(filePath.toString());
                    }
                }
            }
            break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
