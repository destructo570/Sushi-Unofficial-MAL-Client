package com.destructo.sushi.ui.purchaseActivity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.*
import com.destructo.sushi.IS_PRO_USER
import com.destructo.sushi.PRODUCT_ID
import com.destructo.sushi.R
import com.destructo.sushi.databinding.ActivityPurchaseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PurchaseActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var sharedPref: SharedPreferences
    private lateinit var restoreButton:Button
    private lateinit var purchaseButton:Button
    private lateinit var imagePagerAdapter:ImagePagerAdapter
    private lateinit var promoViewPager: ViewPager2
    private lateinit var binding:ActivityPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        createBillingClient()
        initiateBillingProcess()

        promoViewPager = binding.promoImagesViewPager
        restoreButton = binding.restorePurchaseButton
        purchaseButton = binding.purchaseButton
        restoreButton.isEnabled = false
        purchaseButton.isEnabled = false

        val images = listOf(
            R.drawable.app_promo_0,
            R.drawable.app_promo_1,
            R.drawable.app_promo_2,
            R.drawable.app_promo_3,
            R.drawable.app_promo_4,
            R.drawable.app_promo_5,
            R.drawable.app_promo_6

        )
        imagePagerAdapter = ImagePagerAdapter(images)
        promoViewPager.adapter = imagePagerAdapter

        restoreButton.setOnClickListener { queryPurchases() }

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
                    queryProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
            }

        })
    }

    private fun queryPurchases(){
        if(!billingClient.isReady){
            Timber.e("Billing Client Not Ready")
        }

        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (result.purchasesList.isNullOrEmpty()){
            Toast.makeText(this,"No existing purchases found.", Toast.LENGTH_LONG).show()
            sharedPref.edit()?.putBoolean(IS_PRO_USER, false)?.apply()
        }else{
            Timber.e("Existing Purchases: ${result.purchasesList}")
            val purchaseList = result.purchasesList
            purchaseList?.let {
                for (purchase in purchaseList){
                    if (purchase.sku == PRODUCT_ID
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                        ){
                        sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                        Toast.makeText(this,"Purchase restored! Please restart the app.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
    }


    fun queryProducts(){
        val skuList = listOf(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetailsList ->
            if (skuDetailsList != null) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {
                    for (skuDetails in skuDetailsList) {
                        if (skuDetails.sku == PRODUCT_ID) {
                            purchaseButton.setOnClickListener {
                                launchPurchaseFlow(skuDetails)
                            }
                        }
                }
                restoreButton.isEnabled = true
                purchaseButton.isEnabled = true
            } else {
                Toast.makeText(this, "Error retrieving products.",Toast.LENGTH_LONG).show()
                Timber.e("No Sku Found")
            }
        }
    }
    }

    private fun launchPurchaseFlow(skuDetails: SkuDetails){
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(this, flowParams)
        Timber.e("Launch purchase flow result: $responseCode")
    }

    private fun handlePurchase(purchase: Purchase){
        GlobalScope.launch {
            if (purchase.purchaseState  == Purchase.PurchaseState.PURCHASED){
                if (!purchase.isAcknowledged){
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                    sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                    finish()
                    //Add a toast to tell user purchase is complete.
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
            for (purchase in purchases){
                handlePurchase(purchase)
            }
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
            Timber.e("User cancelled the purchase flow.")
        }else{
            Timber.e("onPurchaseUpdatedError : ${billingResult.responseCode}")
        }
    }


}