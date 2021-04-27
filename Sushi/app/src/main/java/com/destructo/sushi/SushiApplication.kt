package com.destructo.sushi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import com.android.billingclient.api.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SushiApplication: Application(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        createBillingClient()
        initiateBillingProcess()
        createNotificationChannels()
    }

    private fun createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val updateChannelName = getString(R.string.notification_channel_update)
            val updateChannelDesc = getString(R.string.notification_channel_update_desc)
            val updateChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val updateChannel = NotificationChannel(UPDATE_CHANNEL_ID, updateChannelName, updateChannelImportance)
            updateChannel.description = updateChannelDesc

            val promotionChannelName = getString(R.string.notification_channel_promotion)
            val promotionChannelDesc = getString(R.string.notification_channel_promotion_desc)
            val promotionChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val promotionChannel = NotificationChannel(PROMOTION_CHANNEL_ID, promotionChannelName, promotionChannelImportance)
            promotionChannel.description = promotionChannelDesc

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(updateChannel)
            notificationManager.createNotificationChannel(promotionChannel)
        }

    }

    private fun createBillingClient(){
        billingClient = BillingClient
            .newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
    }

    private fun initiateBillingProcess(){

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    Timber.e("Billing service successfully setup")
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                Timber.e("Billing service disconnected")
            }

        })
    }

    fun queryPurchases(): Boolean{
        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (result.purchasesList.isNullOrEmpty()){
            sharedPref.edit()?.putBoolean(IS_PRO_USER, false)?.apply()
            return false
        }else{
            val purchaseList = result.purchasesList
            purchaseList?.let {
                for (purchase in purchaseList){
                    return if (purchase.sku == PRODUCT_ID
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ){
                        sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                        true
                    }else{
                        sharedPref.edit()?.putBoolean(IS_PRO_USER, false)?.apply()
                        false
                    }
                }
            }
        }
        return false
    }


    companion object {
        private var instance: SushiApplication? = null

        fun getContext(): SushiApplication {
            return instance!!
        }

        fun isProVersion(): Boolean {
            return instance?.queryPurchases()!!
        }

    }

    private fun handlePurchase(purchase: Purchase){
        GlobalScope.launch {
            if (purchase.purchaseState  == Purchase.PurchaseState.PURCHASED){
                if (!purchase.isAcknowledged){
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                    sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
            for (purchase in purchases){
                handlePurchase(purchase)
            }
        }
    }

}