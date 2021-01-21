package com.destructo.sushi

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.android.billingclient.api.*
import com.destructo.sushi.enum.AppTheme
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class SushiApplication: Application(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        createBillingClient()
        initiateBillingProcess()
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
            sharedPref.edit()?.putString(CURRENT_THEME, AppTheme.LIGHT.value)?.apply()
            return false
        }else{
            val purchaseList = result.purchasesList
            purchaseList?.let {
                for (purchase in purchaseList){
                    if (purchase.sku == PRODUCT_ID
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ){
                        sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                        return true
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