package com.tkrpan.moviedb.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.helpers.PrefsHelper;
import com.tkrpan.moviedb.services.AuthenticatorService;

/**
 * Created by tomislav on 7/29/14. Static helper methods for working with the sync framework.
 */

public class SyncUtils {

    private static final String TAG = "SyncUtils";

    private static final long SYNC_FREQUENCY = 60;

    private static final String CONTENT_AUTHORITY = StaticValue.AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";


    public static void CreateSyncAccount(Context context) {

        Log.i(StaticValue.TOM, TAG + " CreateSyncAccount ");

        boolean newAccount = false;
        boolean setupComplete = PrefsHelper.getBooleanValue(PREF_SETUP_COMPLETE);

        Account account = AuthenticatorService.GetAccount();
        AccountManager accountManager = (AccountManager)context
                .getSystemService(Context.ACCOUNT_SERVICE);

        if(accountManager.addAccountExplicitly(account, null, null)){

            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);

            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);

            ContentResolver.addPeriodicSync(account,
                    CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);

            newAccount = true;
        }

        if (newAccount || !setupComplete) {
            TriggerRefresh();
            PrefsHelper.setBooleanValue(PREF_SETUP_COMPLETE, true);
        }
    }

    public static void TriggerRefresh() {

        Log.i(StaticValue.TOM, TAG + " TriggerRefresh ");

        Bundle bundle = new Bundle();

        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                AuthenticatorService.GetAccount(),      // Sync account
                StaticValue.AUTHORITY,                  // Content authority
                bundle);                                // Extras
    }
}
